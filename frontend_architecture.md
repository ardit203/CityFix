# CityFix — React Frontend Architecture

## Overview

Stack: **React 18 + React Router v6 + TailwindCSS + Vite**

Two root layouts, four role-based dashboard sidebar configs, and a shared component library.

---

## Folder Structure

```
src/
├── assets/                  # Static images, icons, logo
├── components/              # Reusable UI components
│   ├── common/
│   │   ├── Button.jsx
│   │   ├── Badge.jsx          # Status/priority color badges
│   │   ├── Card.jsx           # Stat card wrapper
│   │   ├── Table.jsx          # Generic sortable table
│   │   ├── Modal.jsx
│   │   ├── Spinner.jsx
│   │   ├── Avatar.jsx
│   │   ├── Alert.jsx          # Inline error/success messages
│   │   └── Pagination.jsx
│   ├── forms/
│   │   ├── InputField.jsx
│   │   ├── TextareaField.jsx
│   │   ├── SelectField.jsx
│   │   ├── FileUpload.jsx     # JPG/PNG/PDF, max 5 files
│   │   └── FormError.jsx
│   ├── map/
│   │   └── MapPicker.jsx      # Map location picker placeholder
│   ├── notifications/
│   │   └── NotificationBell.jsx
│   └── requests/
│       ├── RequestStatusBadge.jsx
│       ├── RequestPriorityBadge.jsx
│       ├── RequestTable.jsx
│       ├── RequestFilters.jsx  # Status / Priority / Date / Employee
│       ├── RequestTimeline.jsx # Status history log
│       └── AISuggestionPanel.jsx
├── layouts/
│   ├── AuthLayout.jsx         # Centered card, used for Login/Register/ForgotPassword
│   └── DashboardLayout.jsx    # Navbar + role-aware Sidebar + <Outlet />
├── navigation/
│   ├── Navbar.jsx
│   ├── Sidebar.jsx
│   └── sidebarConfig.js       # Role → nav items mapping
├── pages/
│   ├── auth/
│   │   ├── LoginPage.jsx
│   │   ├── RegisterPage.jsx
│   │   └── ForgotPasswordPage.jsx
│   ├── citizen/
│   │   ├── CitizenDashboardPage.jsx
│   │   ├── SubmitRequestPage.jsx
│   │   ├── MyRequestsPage.jsx
│   │   ├── RequestDetailPage.jsx
│   │   └── NotificationsPage.jsx
│   ├── employee/
│   │   ├── EmployeeDashboardPage.jsx
│   │   ├── AssignedRequestsPage.jsx
│   │   ├── RequestDetailPage.jsx   # Employee view (can add notes/update status)
│   │   └── NotificationsPage.jsx
│   ├── manager/
│   │   ├── ManagerDashboardPage.jsx
│   │   ├── DepartmentRequestsPage.jsx
│   │   ├── ManageRequestPage.jsx   # Review AI suggestion, assign, re-route
│   │   ├── DepartmentStatsPage.jsx
│   │   └── NotificationsPage.jsx
│   ├── admin/
│   │   ├── AdminDashboardPage.jsx
│   │   ├── ManageUsersPage.jsx
│   │   ├── ManageDepartmentsPage.jsx
│   │   ├── ManageCategoriesPage.jsx
│   │   ├── DataImportExportPage.jsx
│   │   └── SystemLogsPage.jsx
│   └── shared/
│       └── ProfilePage.jsx         # Used by all roles
├── context/
│   ├── AuthContext.jsx             # currentUser, role, token
│   └── NotificationContext.jsx
├── hooks/
│   ├── useAuth.js
│   └── useRequests.js
├── services/                       # API call functions (axios wrappers)
│   ├── authService.js
│   ├── requestService.js
│   ├── userService.js
│   ├── departmentService.js
│   ├── categoryService.js
│   └── notificationService.js
├── utils/
│   ├── statusColors.js             # Badge color maps for status/priority
│   └── formatters.js
├── router/
│   └── AppRouter.jsx               # All routes defined here
├── App.jsx
└── main.jsx
```

---

## Layouts

### `AuthLayout`
- Centered on screen (vertically + horizontally)
- White card with shadow
- CityFix logo/title at top
- Renders `<Outlet />`
- Used for: Login, Register, Forgot Password

### `DashboardLayout`
- Top `<Navbar />` with logo, Notifications bell, Profile link
- Left `<Sidebar />` — reads `role` from `AuthContext`, renders role-specific nav items
- Main `<Outlet />` scrollable content area
- Responsive: sidebar collapses on mobile

---

## Sidebar Navigation Config

```js
// navigation/sidebarConfig.js
export const sidebarConfig = {
  CITIZEN: [
    { label: 'Dashboard',       path: '/citizen/dashboard' },
    { label: 'Submit Request',  path: '/citizen/submit' },
    { label: 'My Requests',     path: '/citizen/requests' },
    { label: 'Notifications',   path: '/citizen/notifications' },
    { label: 'Profile',         path: '/profile' },
  ],
  EMPLOYEE: [
    { label: 'Dashboard',         path: '/employee/dashboard' },
    { label: 'Assigned Requests', path: '/employee/requests' },
    { label: 'Notifications',     path: '/employee/notifications' },
    { label: 'Profile',           path: '/profile' },
  ],
  MANAGER: [
    { label: 'Dashboard',         path: '/manager/dashboard' },
    { label: 'Department Requests', path: '/manager/requests' },
    { label: 'Statistics',        path: '/manager/stats' },
    { label: 'Notifications',     path: '/manager/notifications' },
    { label: 'Profile',           path: '/profile' },
  ],
  ADMIN: [
    { label: 'Admin Panel',       path: '/admin/dashboard' },
    { label: 'Manage Users',      path: '/admin/users' },
    { label: 'Departments',       path: '/admin/departments' },
    { label: 'Categories',        path: '/admin/categories' },
    { label: 'Data Management',   path: '/admin/data' },
    { label: 'System Logs',       path: '/admin/logs' },
    { label: 'Profile',           path: '/profile' },
  ],
}
```

---

## Pages Summary by Role

### Authentication (AuthLayout)
| Page | Path | Description |
|------|------|-------------|
| `LoginPage` | `/login` | Username + Password, links to Register & ForgotPassword |
| `RegisterPage` | `/register` | Full Name, Username, Email, Password, Confirm |
| `ForgotPasswordPage` | `/forgot-password` | Email input, sends reset link |

### Citizen (DashboardLayout)
| Page | Path | Description |
|------|------|-------------|
| `CitizenDashboardPage` | `/citizen/dashboard` | 4 stat cards, Quick Actions, Recent Requests table, Status chart |
| `SubmitRequestPage` | `/citizen/submit` | Form: title, description, file upload, map picker, address |
| `MyRequestsPage` | `/citizen/requests` | Filterable table of all user requests |
| `RequestDetailPage` | `/citizen/requests/:id` | Full request detail + timeline |
| `NotificationsPage` | `/citizen/notifications` | Notification list table |

### Employee (DashboardLayout)
| Page | Path | Description |
|------|------|-------------|
| `EmployeeDashboardPage` | `/employee/dashboard` | Assigned request count cards |
| `AssignedRequestsPage` | `/employee/requests` | Filtered list of assigned requests |
| `RequestDetailPage` | `/employee/requests/:id` | Detail + add internal/public notes + update status |
| `NotificationsPage` | `/employee/notifications` | Notification list |

### Manager (DashboardLayout)
| Page | Path | Description |
|------|------|-------------|
| `ManagerDashboardPage` | `/manager/dashboard` | Department overview cards |
| `DepartmentRequestsPage` | `/manager/requests` | Full table with Status/Priority/Date/Employee filters |
| `ManageRequestPage` | `/manager/requests/:id` | AI suggestion panel, assign employee, change priority/category/dept |
| `DepartmentStatsPage` | `/manager/stats` | Stats cards + export button |
| `NotificationsPage` | `/manager/notifications` | Notification list |

### Admin (DashboardLayout)
| Page | Path | Description |
|------|------|-------------|
| `AdminDashboardPage` | `/admin/dashboard` | System overview |
| `ManageUsersPage` | `/admin/users` | User table: Role & Status filters, CRUD actions, Assign Role |
| `ManageDepartmentsPage` | `/admin/departments` | Department table + Add/Edit/Delete |
| `ManageCategoriesPage` | `/admin/categories` | Category table + Add/Edit/Delete |
| `DataImportExportPage` | `/admin/data` | Import CSV/Excel, Export data |
| `SystemLogsPage` | `/admin/logs` | Log table: Timestamp, User, Action |

### Shared
| Page | Path | Description |
|------|------|-------------|
| `ProfilePage` | `/profile` | Name, surname, email, address, profile picture |

---

## Routing Structure (`AppRouter.jsx`)

```jsx
<Routes>
  {/* Auth routes */}
  <Route element={<AuthLayout />}>
    <Route path="/login"           element={<LoginPage />} />
    <Route path="/register"        element={<RegisterPage />} />
    <Route path="/forgot-password" element={<ForgotPasswordPage />} />
  </Route>

  {/* Protected dashboard routes */}
  <Route element={<ProtectedRoute />}>
    <Route element={<DashboardLayout />}>

      {/* Shared */}
      <Route path="/profile" element={<ProfilePage />} />

      {/* Citizen */}
      <Route path="/citizen/dashboard"       element={<CitizenDashboardPage />} />
      <Route path="/citizen/submit"          element={<SubmitRequestPage />} />
      <Route path="/citizen/requests"        element={<MyRequestsPage />} />
      <Route path="/citizen/requests/:id"    element={<CitizenRequestDetailPage />} />
      <Route path="/citizen/notifications"   element={<NotificationsPage />} />

      {/* Employee */}
      <Route path="/employee/dashboard"      element={<EmployeeDashboardPage />} />
      <Route path="/employee/requests"       element={<AssignedRequestsPage />} />
      <Route path="/employee/requests/:id"   element={<EmployeeRequestDetailPage />} />
      <Route path="/employee/notifications"  element={<NotificationsPage />} />

      {/* Manager */}
      <Route path="/manager/dashboard"       element={<ManagerDashboardPage />} />
      <Route path="/manager/requests"        element={<DepartmentRequestsPage />} />
      <Route path="/manager/requests/:id"    element={<ManageRequestPage />} />
      <Route path="/manager/stats"           element={<DepartmentStatsPage />} />
      <Route path="/manager/notifications"   element={<NotificationsPage />} />

      {/* Admin */}
      <Route path="/admin/dashboard"         element={<AdminDashboardPage />} />
      <Route path="/admin/users"             element={<ManageUsersPage />} />
      <Route path="/admin/departments"       element={<ManageDepartmentsPage />} />
      <Route path="/admin/categories"        element={<ManageCategoriesPage />} />
      <Route path="/admin/data"              element={<DataImportExportPage />} />
      <Route path="/admin/logs"              element={<SystemLogsPage />} />

    </Route>
  </Route>

  {/* Redirect root to login */}
  <Route path="/" element={<Navigate to="/login" />} />
</Routes>
```

> **`ProtectedRoute`** checks `AuthContext` for a valid token. After login, redirects to the correct dashboard based on `role`:
> - `CITIZEN` → `/citizen/dashboard`
> - `EMPLOYEE` → `/employee/dashboard`
> - `MANAGER` → `/manager/dashboard`
> - `ADMIN` → `/admin/dashboard`

---

## Key Reusable Components

| Component | Purpose |
|-----------|---------|
| `RequestStatusBadge` | Color-coded pill: gray/blue/green/red per status |
| `RequestPriorityBadge` | LOW (green) / MEDIUM (amber) / HIGH (red) |
| `RequestTable` | Sortable, filterable table used by all roles |
| `RequestFilters` | Dropdown filter bar (Status, Priority, Date, Employee) |
| `RequestTimeline` | Vertical log timeline from `REQUEST_LOGS` |
| `AISuggestionPanel` | Shows suggested category/priority/summary + approve/reject buttons |
| `FileUpload` | Drag-and-drop, accepts JPG/PNG/PDF, max 5 files |
| `MapPicker` | Placeholder for interactive location selection |
| `Card` | Stat card with icon, number, label |
| `Badge` | Generic color badge |
| `Table` | Generic table with column definitions |
| `NotificationBell` | Navbar bell icon with unread count indicator |

---

## Status Color Reference

```js
// utils/statusColors.js
export const statusColors = {
  SUBMITTED:   'bg-gray-100 text-gray-700',
  IN_REVIEW:   'bg-yellow-100 text-yellow-700',
  ASSIGNED:    'bg-purple-100 text-purple-700',
  IN_PROGRESS: 'bg-blue-100 text-blue-700',
  RESOLVED:    'bg-green-100 text-green-700',
  REJECTED:    'bg-red-100 text-red-700',
  CANCELED:    'bg-gray-200 text-gray-500',
}

export const priorityColors = {
  LOW:    'bg-green-100 text-green-700',
  MEDIUM: 'bg-amber-100 text-amber-700',
  HIGH:   'bg-red-100 text-red-700',
}
```
