import React, { useContext } from 'react';
import { ThemeContext } from '../../context/ThemeContext';
import { FaMoon, FaSun } from 'react-icons/fa';

const ThemeToggle = () => {
  const { theme, toggleTheme } = useContext(ThemeContext);
  console.log("theme", theme)

  return (
   <button
  onClick={toggleTheme}
  aria-label="Toggle Theme"
  className="flex items-center justify-center h-8 w-8 rounded-full hover:opacity-90 transition"
>
  {theme === 'dark' ? (
    <FaSun className="text-yellow-400 dark:text-yellow-300 text-xl" />
  ) : (
    <FaMoon className="text-gray-800 dark:text-white text-xl" />
  )}
</button>

  );
};

export default ThemeToggle;
