import React, { useState } from 'react';
import {
  FaBars,
  FaChartBar,
  FaFileAlt,
  FaCog,
  FaBook,
  FaUserGraduate,
} from 'react-icons/fa';
import { FaHouse } from 'react-icons/fa6';
import { Link } from 'react-router-dom';
import ThemeToggle from './ThemeToggle'; // ✅

const Navbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const toggleMenu = () => setMenuOpen(!menuOpen);

  return (
    <nav className="bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-100 fixed top-0 left-0 right-0 z-50 shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex-shrink-0 text-2xl font-bold">LMS</div>

          <div className="hidden md:flex items-center space-x-6">
            <NavLink icon={<FaHouse />} label="Home" to="/" />
            <NavLink icon={<FaChartBar />} label="Dashboard" to="/dashboard" />
            <NavLink icon={<FaBook />} label="Courses" to="/courses" />
            <NavLink icon={<FaUserGraduate />} label="Students" to="/students" />
            <NavLink icon={<FaFileAlt />} label="Reports" to="/reports" />
            <NavLink icon={<FaCog />} label="Settings" to="/settings" />
            <NavLink icon={<FaBook />} label="Courses" to="/courses" />
            <NavLink icon={<FaFileAlt />} label="Add Package" to="/package/create" />
            <NavLink icon={<FaFileAlt />} label="Add Skill" to="/skill/create" />
            <NavLink icon={<FaFileAlt />} label="Add Topic" to="/topic/create" />
            <NavLink icon={<FaFileAlt />} label="Manage Materials" to="/materials" />
            <NavLink icon={<FaFileAlt />} label="Manage Videos" to="/videos" />
            <NavLink icon={<FaFileAlt />} label="Manage MCQs" to="/mcqs" />
            <NavLink icon={<FaFileAlt />} label="Pending Approvals" to="/approvals" />

            <ThemeToggle />
          </div>

          <div className="md:hidden">
            <button onClick={toggleMenu}>
              <FaBars className="text-2xl" />
            </button>
          </div>
        </div>
      </div>

      {menuOpen && (
        <div className="md:hidden px-4 pb-4 space-y-2 bg-white dark:bg-gray-800">
          <NavLink icon={<FaHouse />} label="Home" to="/" mobile />
          <NavLink icon={<FaChartBar />} label="Dashboard" to="/dashboard" mobile />
          <NavLink icon={<FaBook />} label="Courses" to="/courses" mobile />
          <NavLink icon={<FaUserGraduate />} label="Students" to="/students" mobile />
          <NavLink icon={<FaFileAlt />} label="Reports" to="/reports" mobile />
          <NavLink icon={<FaCog />} label="Settings" to="/settings" mobile />

          <NavLink icon={<FaBook />} label="Courses" to="/courses" />
          <NavLink icon={<FaFileAlt />} label="Add Package" to="/package/create" />
          <NavLink icon={<FaFileAlt />} label="Add Skill" to="/skill/create" />
          <NavLink icon={<FaFileAlt />} label="Add Topic" to="/topic/create" />
          <NavLink icon={<FaFileAlt />} label="Manage Materials" to="/materials" />
          <NavLink icon={<FaFileAlt />} label="Manage Videos" to="/videos" />
          <NavLink icon={<FaFileAlt />} label="Manage MCQs" to="/mcqs" />
          <NavLink icon={<FaFileAlt />} label="Pending Approvals" to="/approvals" />
          <ThemeToggle />
        </div>
      )}
    </nav>
  );
};

const NavLink = ({ icon, label, to, mobile }) => (
  <Link
    to={to}
    className={`flex items-center space-x-2 hover:bg-gray-200 dark:hover:bg-gray-700 rounded-md ${mobile ? 'p-2' : 'px-3 py-2'
      } text-gray-900 dark:text-gray-100`}
  >
    <span className="text-lg">{icon}</span>
    <span className="text-md">{label}</span>
  </Link>
);

export default Navbar;
