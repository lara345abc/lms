import React from 'react';

const SearchAndSortBar = ({
  searchTerm,
  onSearchChange,
  sortOption,
  onSortChange,
  placeholder = "Search..."
}) => {
    console.log("inside search and sort component")
  return (
    <div className="flex flex-col md:flex-row gap-4 justify-center mb-10">
      <input
        type="text"
        placeholder={placeholder}
        value={searchTerm}
        onChange={(e) => onSearchChange(e.target.value)}
        className="w-full md:w-1/2 p-3 border border-gray-300 dark:border-gray-600 rounded-lg shadow-sm 
                   bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 
                   focus:outline-none focus:ring-2 focus:ring-indigo-400 dark:focus:ring-indigo-500"
      />
      <select
        value={sortOption}
        onChange={(e) => onSortChange(e.target.value)}
        className="w-full md:w-1/3 p-3 border border-gray-300 dark:border-gray-600 rounded-lg shadow-sm 
                   bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
      >
        <option value="">Sort by</option>
        <option value="title-asc">Name (A–Z)</option>
        <option value="title-desc">Name (Z–A)</option>
        <option value="date-newest">Newest First</option>
        <option value="date-oldest">Oldest First</option>
      </select>
    </div>
  );
};

export default SearchAndSortBar;
