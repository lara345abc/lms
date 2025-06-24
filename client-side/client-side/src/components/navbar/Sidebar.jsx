import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import {
  FaChevronDown,
  FaChevronRight,
  FaFileAlt,
  FaBars,
  FaCog
} from 'react-icons/fa';
import { FaHouse, FaPerson } from 'react-icons/fa6';
import ThemeToggle from './ThemeToggle';
import userService from '../../services/userService';

const Sidebar = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false);
  const [contentOpen, setContentOpen] = useState(false);
  const [user, setUser] = useState({});
  const [role, setRole] = useState('');
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    fetchUser();
    const storedRole = localStorage.getItem('role');
    setRole(storedRole);
  }, []);

  const fetchUser = async () => {
    try {
      const data = await userService.getUserByEmail();
      setUser(data.data);
    } catch (error) {
      console.log("Error fetching User Details", error);
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('user');
    navigate('/');
  };

  const isActive = (path) => location.pathname.startsWith(path);
  const toggleSidebar = () => setCollapsed(!collapsed);
  const toggleContentDropdown = () => setContentOpen(!contentOpen);

  return (
    <div className="flex h-screen bg-gray-100 dark:bg-gray-900 overflow-hidden">
      {/* Sidebar */}
      <div className={`transition-all duration-300 ${collapsed ? 'w-16' : 'w-64'} bg-gray-200 dark:bg-gray-800 shadow-lg flex flex-col`}>
        {/* Header */}
        <div className="flex items-center justify-between p-3">
          {!collapsed && <span className="text-xl font-bold text-primary dark:text-white font-mono">Lara&nbsp;Technologies</span>}
          <ThemeToggle />
        </div>

        {/* Nav Links */}
        <div className="flex-1 overflow-y-auto px-2 space-y-1 text-sm">
          <NavLink icon={<FaHouse />} label="Home" to="/user/my-dashboard" collapsed={collapsed} isActive={isActive('/dashboard')} />
          <NavLink icon={<FaCog />} label="My Packages" to="/user/packages" collapsed={collapsed} isActive={isActive('/user/packages')} />
          <NavLink icon={<FaCog />} label="Settings" to="/user/settings" collapsed={collapsed} isActive={isActive('/user/settings')} />

          {role !== 'USER' && (
            <>
              <NavLink icon={<FaPerson />} label="Users Details" to="/admin/paginated-users" collapsed={collapsed} isActive={isActive('/admin/paginated-users')} />
              <NavLink icon={<FaFileAlt />} label="Reports" to="/reports" collapsed={collapsed} isActive={isActive('/reports')} />
            </>
          )}

          {role !== 'USER' && (
            <div>
              <button
                onClick={toggleContentDropdown}
                className="flex items-center w-full space-x-2 px-3 py-2 hover:bg-gray-300 dark:hover:bg-gray-700 rounded text-gray-900 dark:text-gray-100"
              >
                <FaFileAlt className="text-sm" />
                {!collapsed && <span className="text-sm flex-1 text-left">Manage Content</span>}
                {!collapsed && (contentOpen ? <FaChevronDown /> : <FaChevronRight />)}
              </button>
              {contentOpen && !collapsed && (
                <div className="pl-6 space-y-1">
                  {/* <NavLink icon={<FaFileAlt />} label="Add Package" to="/package/create" collapsed={false} isActive={isActive('/package/create')} /> */}
                  <NavLink icon={<FaPerson />} label="Assign Packages" to="/admin/package-assigner" collapsed={collapsed} isActive={isActive('/admin/package-assigner')} />
                  <NavLink icon={<FaPerson />} label="Upload Content" to="/package" collapsed={collapsed} isActive={isActive('/package')} />
                </div>
              )}
            </div>
          )}

          {/* Logout Button */}
          {/* <div className="absolute bottom-0 left-0 right-0 p-3"> */}
          <button
            onClick={logout}
            className=" px-4 py-2 rounded bg-red-500 text-white hover:bg-red-600 transition-colors bottom-0  mb-3 ms-3"
          >
            Logout
          </button>
          {/* </div> */}
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Top bar toggle */}
        <div className="h-12 flex items-center justify-between px-4 bg-gray-200 dark:bg-gray-800 shadow">
          <button onClick={toggleSidebar} className="text-xl text-gray-900 dark:text-white">
            <FaBars />
          </button>
          <div>
            <img
              alt=""
              src={user.pictureUrl}
              className="inline-block size-8 mr-2 rounded-full ring-2 ring-white"
              referrerPolicy="no-referrer"
            />
          </div>
        </div>

        {/* Routed Page */}
        <main className="flex-1 overflow-y-auto bg-gray-100 dark:bg-gray-900 text-gray-900 dark:text-gray-100">
          {children}
        </main>
      </div>
    </div>
  );
};

const NavLink = ({ icon, label, to, collapsed, isActive }) => (
  <Link
    to={to}
    className={`flex items-center space-x-2 rounded px-3 py-2 
      hover:bg-gray-300 dark:hover:bg-gray-700 
      text-gray-900 dark:text-gray-100 
      ${isActive ? 'bg-gray-300 dark:bg-gray-700' : ''}`}
  >
    <span className="text-md">{icon}</span>
    {!collapsed && <span className="text-sm">{label}</span>}
  </Link>
);

export default Sidebar;
