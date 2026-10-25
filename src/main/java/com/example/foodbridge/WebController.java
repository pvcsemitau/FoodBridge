package com.foodbridge.controller;

import com.foodbridge.model.*;
import com.foodbridge.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WebController {
    private final UserRepository users;
    private final DonationRepository donations;
    private final BCryptPasswordEncoder encoder;

    public WebController(UserRepository users, DonationRepository donations, BCryptPasswordEncoder encoder) {
        this.users=users; this.donations=donations; this.encoder=encoder;
    }
    private AppUser current(HttpSession session) { Object id=session.getAttribute("userId"); return id instanceof Long ? users.findById((Long)id).orElse(null) : null; }
    private String roleRedirect(AppUser u) { return u.getRole()==Role.ADMIN ? "redirect:/admin" : u.getRole()==Role.NGO ? "redirect:/ngo" : "redirect:/restaurant"; }
    @GetMapping("/") public String home(Model m) {
        List<Donation> done=donations.findAll().stream().filter(d->d.getStatus()==DonationStatus.COMPLETED).toList();
        m.addAttribute("completedCount",done.size());
        m.addAttribute("foodRescued",done.stream().map(Donation::getQuantity).reduce(BigDecimal.ZERO,BigDecimal::add));
        m.addAttribute("restaurantCount",users.findByRoleOrderByCreatedAtDesc(Role.RESTAURANT).size());
        m.addAttribute("ngoCount",users.findByRoleOrderByCreatedAtDesc(Role.NGO).stream().filter(AppUser::isVerified).count());
        return "index";
    }
    @GetMapping("/register") public String registerPage(Model m){m.addAttribute("error",null);return "register";}
    @PostMapping("/register") public String register(@RequestParam String organizationName,@RequestParam String location,
      @RequestParam String email,@RequestParam String password,@RequestParam Role role, RedirectAttributes ra) {
        email=email.trim().toLowerCase();
        if(role==Role.ADMIN){ra.addFlashAttribute("error","Admin accounts cannot be registered publicly.");return "redirect:/register";}
        if(organizationName.isBlank()||location.isBlank()||password.length()<8){ra.addFlashAttribute("error","Complete all fields. Password must contain at least 8 characters.");return "redirect:/register";}
        if(users.existsByEmailIgnoreCase(email)){ra.addFlashAttribute("error","An account with this email already exists.");return "redirect:/register";}
        AppUser u=new AppUser();u.setOrganizationName(organizationName.trim());u.setLocation(location.trim());u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));u.setRole(role);u.setVerified(role==Role.RESTAURANT);users.save(u);
        ra.addFlashAttribute("success",role==Role.NGO?"Account created. Please wait for admin verification.":"Account created. You can now sign in.");
        return "redirect:/login";
    }
    @GetMapping("/login") public String loginPage(Model m){return "login";}
    @PostMapping("/login") public String login(@RequestParam String email,@RequestParam String password,HttpSession session,RedirectAttributes ra){
        Optional<AppUser> found=users.findByEmailIgnoreCase(email.trim());
        if(found.isEmpty()||!encoder.matches(password,found.get().getPasswordHash())){ra.addFlashAttribute("error","Invalid email or password.");return "redirect:/login";}
        session.setAttribute("userId",found.get().getId()); return roleRedirect(found.get());
    }
    @PostMapping("/logout") public String logout(HttpSession session){session.invalidate();return "redirect:/";}
    @GetMapping("/restaurant") public String restaurant(HttpSession s,Model m,RedirectAttributes ra){
        AppUser u=current(s);if(u==null)return "redirect:/login";if(u.getRole()!=Role.RESTAURANT)return roleRedirect(u);
        List<Donation> mine=donations.findByRestaurantOrderByCreatedAtDesc(u);expireAvailable();m.addAttribute("user",u);m.addAttribute("donations",mine);
        m.addAttribute("active",mine.stream().filter(d->d.getStatus()==DonationStatus.AVAILABLE).count());
        m.addAttribute("completed",mine.stream().filter(d->d.getStatus()==DonationStatus.COMPLETED).count());return "restaurant";
    }
    @PostMapping("/donations") public String createDonation(HttpSession s,@RequestParam String foodName,@RequestParam String foodType,
      @RequestParam BigDecimal quantity,@RequestParam String unit,@RequestParam String pickupLocation,
      @RequestParam String preparedAt,@RequestParam String expiryAt,RedirectAttributes ra){
        AppUser u=current(s);if(u==null)return "redirect:/login";if(u.getRole()!=Role.RESTAURANT)return roleRedirect(u);
        try {
            LocalDateTime prep=LocalDateTime.parse(preparedAt), exp=LocalDateTime.parse(expiryAt);
            if(foodName.isBlank()||pickupLocation.isBlank()||quantity.compareTo(BigDecimal.ZERO)<=0||prep.isAfter(LocalDateTime.now().plusMinutes(5))||!exp.isAfter(prep)||!exp.isAfter(LocalDateTime.now()))
                throw new IllegalArgumentException();
            Donation d=new Donation();d.setRestaurant(u);d.setFoodName(foodName.trim());d.setFoodType(foodType);d.setQuantity(quantity);d.setUnit(unit);
            d.setPickupLocation(pickupLocation.trim());d.setPreparedAt(prep);d.setExpiryAt(exp);d.setStatus(DonationStatus.AVAILABLE);donations.save(d);
            ra.addFlashAttribute("success","Donation published successfully.");
        } catch(Exception ex){ra.addFlashAttribute("error","Check the quantity, preparation time and expiry time. Expiry must be after preparation and in the future.");}
        return "redirect:/restaurant";
    }
    @GetMapping("/ngo") public String ngo(HttpSession s,Model m,@RequestParam(required=false) String q,
      @RequestParam(required=false) String type,@RequestParam(required=false) BigDecimal min,@RequestParam(required=false) BigDecimal max){
        AppUser u=current(s);if(u==null)return "redirect:/login";if(u.getRole()!=Role.NGO)return roleRedirect(u);expireAvailable();
        List<Donation> all=donations.findByStatusOrderByExpiryAtAsc(DonationStatus.AVAILABLE).stream().filter(d->d.getExpiryAt().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        if(q!=null&&!q.isBlank()){String term=q.toLowerCase();all=all.stream().filter(d->d.getFoodName().toLowerCase().contains(term)||d.getRestaurant().getOrganizationName().toLowerCase().contains(term)||d.getPickupLocation().toLowerCase().contains(term)).toList();}
        if(type!=null&&!type.isBlank())all=all.stream().filter(d->d.getFoodType().equals(type)).toList();
        if(min!=null)all=all.stream().filter(d->d.getQuantity().compareTo(min)>=0).toList();
        if(max!=null)all=all.stream().filter(d->d.getQuantity().compareTo(max)<=0).toList();
        m.addAttribute("user",u);m.addAttribute("available",all);m.addAttribute("mine",donations.findByNgoOrderByCreatedAtDesc(u));
        m.addAttribute("verified",u.isVerified());m.addAttribute("q",q);m.addAttribute("type",type);m.addAttribute("min",min);m.addAttribute("max",max);return "ngo";
    }
    @PostMapping("/donations/{id}/accept") public String accept(@PathVariable Long id,HttpSession s,RedirectAttributes ra){
        AppUser u=current(s);if(u==null)return "redirect:/login";if(u.getRole()!=Role.NGO)return roleRedirect(u);
        if(!u.isVerified()){ra.addFlashAttribute("error","Your NGO must be verified before accepting donations.");return "redirect:/ngo";}
        Donation d=donations.findById(id).orElse(null);
        if(d==null||d.getStatus()!=DonationStatus.AVAILABLE||!d.getExpiryAt().isAfter(LocalDateTime.now())){ra.addFlashAttribute("error","Donation is no longer available.");return "redirect:/ngo";}
        d.setNgo(u);d.setStatus(DonationStatus.ACCEPTED);donations.save(d);return "redirect:/ngo";
    }
    @PostMapping("/donations/{id}/status") public String updateStatus(@PathVariable Long id,@RequestParam DonationStatus status,HttpSession s,RedirectAttributes ra){
        AppUser u=current(s);if(u==null)return "redirect:/login";if(u.getRole()!=Role.NGO)return roleRedirect(u);
        Donation d=donations.findById(id).orElse(null);
        if(d==null||d.getNgo()==null||!d.getNgo().getId().equals(u.getId())){ra.addFlashAttribute("error","Donation not assigned to your NGO.");return "redirect:/ngo";}
        boolean valid=(d.getStatus()==DonationStatus.ACCEPTED&&status==DonationStatus.PICKED_UP)||(d.getStatus()==DonationStatus.PICKED_UP&&status==DonationStatus.COMPLETED);
        if(valid){d.setStatus(status);donations.save(d);}else ra.addFlashAttribute("error","Invalid status transition.");
        return "redirect:/ngo";
    }
    @GetMapping("/admin") public String admin(HttpSession s,Model m){
        AppUser u=current(s);if(u==null)return "redirect:/login";if(u.getRole()!=Role.ADMIN)return roleRedirect(u);expireAvailable();
        m.addAttribute("user",u);m.addAttribute("users",users.findAll());m.addAttribute("pendingNgos",users.findByRoleOrderByCreatedAtDesc(Role.NGO).stream().filter(x->!x.isVerified()).toList());
        m.addAttribute("donations",donations.findAll());m.addAttribute("restaurantCount",users.findByRoleOrderByCreatedAtDesc(Role.RESTAURANT).size());
        m.addAttribute("ngoCount",users.findByRoleOrderByCreatedAtDesc(Role.NGO).size());m.addAttribute("completedCount",donations.countByStatus(DonationStatus.COMPLETED));return "admin";
    }
    @PostMapping("/admin/verify/{id}") public String verify(@PathVariable Long id,HttpSession s,RedirectAttributes ra){
        AppUser admin=current(s);if(admin==null)return "redirect:/login";if(admin.getRole()!=Role.ADMIN)return roleRedirect(admin);
        AppUser u=users.findById(id).orElse(null);if(u!=null&&u.getRole()==Role.NGO){u.setVerified(true);users.save(u);ra.addFlashAttribute("success","NGO verified.");}return "redirect:/admin";
    }
    private void expireAvailable(){for(Donation d:donations.findByStatusOrderByExpiryAtAsc(DonationStatus.AVAILABLE))if(!d.getExpiryAt().isAfter(LocalDateTime.now())){d.setStatus(DonationStatus.EXPIRED);donations.save(d);}}
}
