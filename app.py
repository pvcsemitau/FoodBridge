import streamlit as st
import requests
import pandas as pd

# =========================================================
# PAGE CONFIGURATION
# =========================================================
st.set_page_config(
    page_title="FoodBridge Management Dashboard",
    page_icon="🍲",
    layout="wide",
    initial_sidebar_state="expanded"
)

# Backend API Base URL (Change this to your deployed backend if needed)
API_URL = "http://localhost:8080"

# =========================================================
# PROFESSIONAL CUSTOM THEME
# =========================================================
st.markdown("""
<style>
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap');

    html, body, [class*="css"] {
        font-family: 'Inter', sans-serif;
    }

    .stApp {
        background: linear-gradient(135deg, #fdfbf7 0%, #f4f8f4 100%);
    }

    /* Sidebar */
    [data-testid="stSidebar"] {
        background: linear-gradient(180deg, #132e1d 0%, #1c452c 100%);
    }

    [data-testid="stSidebar"] * {
        color: #f8fafc !important;
    }

    [data-testid="stSidebar"] hr {
        border-color: #2e5a3f;
    }

    /* Navigation dropdown box */
    [data-testid="stSidebar"] [data-baseweb="select"] {
        background: #ffffff !important;
        border-radius: 10px !important;
    }

    [data-testid="stSidebar"] [data-baseweb="select"] > div {
        background-color: #ffffff !important;
        border: 1px solid #cbd5e1 !important;
        border-radius: 10px !important;
        box-shadow: none !important;
    }

    /* Selected navigation text: BLACK */
    [data-testid="stSidebar"] [data-baseweb="select"] [class*="singleValue"],
    [data-testid="stSidebar"] [data-baseweb="select"] [class*="ValueContainer"] > div {
        color: #000000 !important;
        -webkit-text-fill-color: #000000 !important;
        opacity: 1 !important;
    }

    /* Dropdown arrow */
    [data-testid="stSidebar"] [data-baseweb="select"] svg {
        color: #000000 !important;
        fill: #000000 !important;
    }

    /* Dropdown options */
    [data-baseweb="popover"] [role="option"] {
        background-color: #ffffff !important;
        color: #000000 !important;
    }

    [data-baseweb="popover"] [role="option"] * {
        color: #000000 !important;
        -webkit-text-fill-color: #000000 !important;
    }

    /* Hovered dropdown option */
    [data-baseweb="popover"] [role="option"]:hover {
        background-color: #e2f8eb !important;
        color: #000000 !important;
    }

    /* Headings */
    h1, h2, h3 {
        color: #132e1d;
        font-weight: 800 !important;
    }

    /* Hero banner */
    .hero {
        background: linear-gradient(115deg, #132e1d 0%, #205c38 65%, #2d8a52 100%);
        padding: 30px 32px;
        border-radius: 20px;
        color: white;
        margin-bottom: 24px;
        box-shadow: 0 10px 28px rgba(32, 92, 56, 0.18);
    }

    .hero h1 {
        color: white !important;
        font-size: 30px;
        margin-bottom: 8px;
    }

    .hero p {
        color: #e2f8eb;
        font-size: 15px;
        margin-bottom: 0;
    }

    /* Section label */
    .section-label {
        color: #3b7a57;
        font-size: 12px;
        font-weight: 800;
        letter-spacing: 2px;
        text-transform: uppercase;
        margin-bottom: 6px;
    }

    /* Dashboard metrics */
    div[data-testid="stMetric"] {
        background: white;
        padding: 20px;
        border-radius: 16px;
        border: 1px solid #e1efe5;
        box-shadow: 0 5px 18px rgba(31, 100, 60, 0.06);
    }

    div[data-testid="stMetricLabel"] {
        color: #52735f;
        font-weight: 600;
    }

    div[data-testid="stMetricValue"] {
        color: #1c452c;
        font-weight: 800;
    }

    /* Information cards */
    .info-card {
        background: white;
        padding: 20px;
        border-radius: 16px;
        border: 1px solid #e1efe5;
        box-shadow: 0 5px 18px rgba(31, 100, 60, 0.05);
        min-height: 120px;
        margin-bottom: 10px;
    }

    .info-card h3 {
        font-size: 17px;
        margin-bottom: 8px;
    }

    .info-card p {
        color: #64748b;
        font-size: 13px;
        margin-bottom: 0;
    }

    /* Buttons */
    div.stButton > button {
        border-radius: 10px;
        border: 1px solid #d5eade;
        font-weight: 700;
        min-height: 45px;
        transition: all 0.2s ease;
    }

    div.stButton > button:hover {
        border-color: #2d8a52;
        color: #205c38;
        box-shadow: 0 4px 12px rgba(45, 138, 82, 0.12);
    }

    div.stButton > button[kind="primary"],
    div.stFormSubmitButton > button[kind="primary"] {
        background: linear-gradient(100deg, #205c38, #2d8a52);
        color: white;
        border: none;
    }

    /* Data tables */
    div[data-testid="stDataFrame"] {
        border: 1px solid #e1efe5;
        border-radius: 12px;
        overflow: hidden;
    }

    /* Forms */
    div[data-testid="stForm"] {
        background: rgba(255, 255, 255, 0.9);
        border: 1px solid #e1efe5;
        padding: 20px;
        border-radius: 16px;
    }

    /* Alerts and dividers */
    [data-testid="stAlert"] {
        border-radius: 12px;
    }

    hr {
        border-color: #d8e8dc;
    }

    /* Footer */
    .footer {
        text-align: center;
        color: #7d9685;
        font-size: 12px;
        padding: 24px 0 8px;
    }
</style>
""", unsafe_allow_html=True)


# =========================================================
# HELPER FUNCTIONS & NAVIGATION
# =========================================================
MENU_OPTIONS = [
    "Home",
    "System Status",
    "User Management",
    "Donations Overview"
]

def navigate_to(page):
    """Change the sidebar selection before the next rerun."""
    st.session_state["selected_menu"] = page


def fetch_records(endpoint):
    """Fetch a list of records from the Spring Boot API."""
    response = requests.get(f"{API_URL}/{endpoint}", timeout=10)
    response.raise_for_status()
    data = response.json()

    if isinstance(data, list):
        return data

    return data.get(endpoint, [])


def show_connection_error():
    st.error(
        f"Cannot connect to the backend at `{API_URL}`. "
        "Please make sure your Spring Boot server is running."
    )


def show_page_header(title, subtitle, icon):
    st.markdown(
        f"""
        <div class="hero">
            <h1>{icon} {title}</h1>
            <p>{subtitle}</p>
        </div>
        """,
        unsafe_allow_html=True
    )


def show_dataframe(records):
    """Display records and offer a CSV download."""
    df = pd.DataFrame(records)

    st.dataframe(
        df,
        use_container_width=True,
        hide_index=True
    )

    st.download_button(
        "⬇️ Download CSV",
        data=df.to_csv(index=False).encode("utf-8"),
        file_name="foodbridge_records.csv",
        mime="text/csv"
    )


# =========================================================
# SIDEBAR NAVIGATION
# =========================================================
with st.sidebar:
    st.markdown(
        """
        <div style="text-align:center; padding:18px 4px 22px;">
            <div style="font-size:42px;">🍲</div>
            <div style="font-size:19px;font-weight:800;">
                FOODBRIDGE
            </div>
            <div style="font-size:11px;color:#a3c9b3;">
                MANAGEMENT PORTAL
            </div>
        </div>
        """,
        unsafe_allow_html=True
    )

    st.divider()
    st.markdown("### 🧭 Navigation")

    if "selected_menu" not in st.session_state:
        st.session_state["selected_menu"] = "Home"

    menu = st.selectbox(
        "Choose a module",
        MENU_OPTIONS,
        key="selected_menu"
    )

    st.divider()

    st.markdown(
        """
        <div style="font-size:12px;line-height:2;">
            <b>PROJECT MODULES</b><br>
            🩺 System Health<br>
            👥 User Directory<br>
            📦 Donations Feed
        </div>
        """,
        unsafe_allow_html=True
    )

    st.divider()

    st.caption("FoodBridge Surplus Redistribution")
    st.caption("Community Sustainability Platform")


# =========================================================
# TOP HEADER
# =========================================================
st.markdown(
    """
    <div style="display:flex;justify-content:space-between;
                align-items:center;margin-bottom:16px;">
        <div>
            <div style="color:#52735f;font-size:12px;
                        font-weight:700;letter-spacing:1.5px;">
                SUSTAINABILITY & SURPLUS REDISTRIBUTION
            </div>
            <div style="color:#132e1d;font-size:23px;
                        font-weight:800;">
                FoodBridge Management Dashboard
            </div>
        </div>
    </div>
    """,
    unsafe_allow_html=True
)


# =========================================================
# HOME DASHBOARD
# =========================================================
if menu == "Home":
    show_page_header(
        "FoodBridge Dashboard",
        "Manage registered partner organizations, food surplus listings, and server health from one place.",
        "🍲"
    )

    user_count = 0
    donation_count = 0
    users_loaded = False
    donations_loaded = False

    try:
        users = fetch_records("api/users")
        user_count = len(users)
        users_loaded = True
    except requests.exceptions.RequestException:
        pass
    except (ValueError, TypeError):
        pass

    try:
        donations = fetch_records("api/donations")
        donation_count = len(donations)
        donations_loaded = True
    except requests.exceptions.RequestException:
        pass
    except (ValueError, TypeError):
        pass

    st.markdown(
        '<div class="section-label">OVERVIEW AT A GLANCE</div>',
        unsafe_allow_html=True
    )

    c1, c2, c3 = st.columns(3)

    with c1:
        st.metric(
            "👥 Registered Users",
            user_count if users_loaded else "—"
        )

    with c2:
        st.metric(
            "📦 Active Donations",
            donation_count if donations_loaded else "—"
        )

    with c3:
        st.metric(
            "🔌 Backend API Status",
            "Online" if users_loaded or donations_loaded else "Check Connection"
        )

    st.write("")
    st.markdown(
        '<div class="section-label">QUICK ACTIONS</div>',
        unsafe_allow_html=True
    )

    q1, q2 = st.columns(2)

    with q1:
        st.markdown(
            """
            <div class="info-card">
                <h3>🩺 System Status</h3>
                <p>Check if your Spring Boot REST server and health actuators are responding.</p>
            </div>
            """,
            unsafe_allow_html=True
        )

        st.button(
            "Open System Status →",
            key="quick_status",
            use_container_width=True,
            on_click=navigate_to,
            args=("System Status",)
        )

        st.markdown(
            """
            <div class="info-card">
                <h3>📦 Donations Overview</h3>
                <p>Browse active food surplus listings, redistribution feeds, and claims.</p>
            </div>
            """,
            unsafe_allow_html=True
        )

        st.button(
            "View Donations →",
            key="quick_donations",
            use_container_width=True,
            on_click=navigate_to,
            args=("Donations Overview",)
        )

    with q2:
        st.markdown(
            """
            <div class="info-card">
                <h3>👥 User Management</h3>
                <p>View registered platform organizations, users, and register new accounts.</p>
            </div>
            """,
            unsafe_allow_html=True
        )

        st.button(
            "Open User Directory →",
            key="quick_users",
            type="primary",
            use_container_width=True,
            on_click=navigate_to,
            args=("User Management",)
        )

    st.markdown(
        """
        <div class="footer">
            FoodBridge System · Sustainable Food Redistribution Network
        </div>
        """,
        unsafe_allow_html=True
    )


# =========================================================
# SYSTEM STATUS
# =========================================================
elif menu == "System Status":
    show_page_header(
        "Backend Health & Connectivity",
        "Monitor server responsiveness and Spring Boot Actuator diagnostics.",
        "🩺"
    )

    st.markdown("### 🔌 Server Connection Test")
    
    if st.button("Ping Backend Server", type="primary"):
        try:
            response = requests.get(f"{API_URL}/actuator/health", timeout=5)
            if response.status_code == 200:
                st.success("Connected successfully to Spring Boot Backend!")
                st.json(response.json())
            else:
                st.warning(f"Backend responded with status code: {response.status_code}")
        except requests.exceptions.ConnectionError:
            show_connection_error()
        except Exception as e:
            st.error(f"An error occurred: {e}")


# =========================================================
# USER MANAGEMENT
# =========================================================
elif menu == "User Management":
    show_page_header(
        "User Directory",
        "View registered platform organizations and register new users.",
        "👥"
    )

    st.markdown("### ➕ Register a New User")

    with st.form("new_user_form", clear_on_submit=True):
        col1, col2 = st.columns(2)
        with col1:
            org_name = st.text_input("Organization Name", placeholder="e.g. City Food Bank")
            email = st.text_input("Email Address", placeholder="contact@foodbank.org")
        with col2:
            password = st.text_input("Password", type="password", placeholder="Enter secure password")
            location = st.text_input("Location", placeholder="e.g. Downtown Area")
            
        submit = st.form_submit_button("Create User", type="primary", use_container_width=True)
        if submit:
            if org_name and email and password:
                try:
                    payload = {
                        "organizationName": org_name,
                        "email": email,
                        "password": password,
                        "location": location
                    }
                    response = requests.post(f"{API_URL}/api/users", json=payload, timeout=5)
                    if response.status_code in [200, 201]:
                        st.success("User successfully registered!")
                        st.rerun()
                    else:
                        st.error(f"Failed to register user. Server response: {response.text}")
                except requests.exceptions.ConnectionError:
                    show_connection_error()
                except Exception as ex:
                    st.error(f"Connection error: {ex}")
            else:
                st.warning("Please fill out all required fields.")

    st.divider()
    st.markdown("### 📑 Registered Users")

    if st.button("🔄 Refresh User Directory"):
        st.rerun()

    try:
        res = requests.get(f"{API_URL}/api/users", timeout=5)
        if res.status_code == 200:
            users = res.json()
            if users:
                st.caption(f"Total registered users: {len(users)}")
                show_dataframe(users)
            else:
                st.info("No users found in the database.")
        else:
            st.info("Users endpoint responded, but no data was returned or endpoint needs configuration.")
    except requests.exceptions.ConnectionError:
        show_connection_error()
    except Exception:
        st.warning("⚠️ Could not fetch users list. Ensure your `/api/users` REST controller is implemented and backend is active.")


# =========================================================
# DONATIONS OVERVIEW
# =========================================================
elif menu == "Donations Overview":
    show_page_header(
        "Food Redistribution Feed",
        "Monitor active food surplus listings and claims in real time.",
        "📦"
    )

    c1, c2 = st.columns([3, 1])

    with c1:
        st.markdown("### 📋 Active Surplus Listings")

    with c2:
        if st.button("🔄 Refresh Listings", use_container_width=True):
            st.rerun()

    try:
        res = requests.get(f"{API_URL}/api/donations", timeout=5)
        if res.status_code == 200:
            donations = res.json()
            if donations:
                st.caption(f"Total active listings: {len(donations)}")
                show_dataframe(donations)
            else:
                st.info("No active donations listed.")
        else:
            st.info("Donations endpoint is ready for integration.")
    except requests.exceptions.ConnectionError:
        show_connection_error()
    except Exception:
        st.warning("⚠️ Could not fetch donations from backend. Ensure your backend controller is active.")


# =========================================================
# FOOTER
# =========================================================
if menu != "Home":
    st.markdown(
        """
        <div class="footer">
            FoodBridge System · Sustainable Food Redistribution Network
        </div>
        """,
        unsafe_allow_html=True
    )