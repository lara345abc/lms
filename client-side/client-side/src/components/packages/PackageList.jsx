import React, { useEffect, useState } from 'react';
import { getAllPackages, deletePackage } from '../../services/packageService';
import { Link } from 'react-router-dom';

const PackageList = () => {
  const [packages, setPackages] = useState([]);
  const [filteredPackages, setFilteredPackages] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortOption, setSortOption] = useState('');

  const fetchPackages = async () => {
    try {
      const { data } = await getAllPackages();
      setPackages(data.data);
      setFilteredPackages(data.data);
    } catch (error) {
      console.error('Error fetching packages', error);
    }
  };

  useEffect(() => {
    fetchPackages();
  }, []);

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this package?')) {
      try {
        await deletePackage(id);
        fetchPackages();
      } catch (error) {
        console.error('Error deleting package', error);
      }
    }
  };

  const handleSearch = (e) => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);
    const filtered = packages.filter((pkg) =>
      pkg.title.toLowerCase().includes(term) ||
      pkg.skills.some((skill) => skill.title.toLowerCase().includes(term))
    );
    setFilteredPackages(filtered);
  };

  const handleSort = (e) => {
    const option = e.target.value;
    setSortOption(option);

    const sorted = [...filteredPackages].sort((a, b) => {
      if (option === 'name-asc') return a.title.localeCompare(b.title);
      if (option === 'name-desc') return b.title.localeCompare(a.title);
      if (option === 'date-asc') return new Date(a.createdAt) - new Date(b.createdAt);
      if (option === 'date-desc') return new Date(b.createdAt) - new Date(a.createdAt);
      return 0;
    });

    setFilteredPackages(sorted);
  };

  return (
    <div className="p-6 bg-white dark:bg-gray-900 min-h-screen text-gray-900 dark:text-white">
      <div className="flex flex-col md:flex-row md:justify-between md:items-center mb-6 gap-4">
        <h1 className="text-3xl font-bold">Manage Learning Packages</h1>
        <Link
          to="/package/create"
          className="bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
        >
          + Create Package
        </Link>
      </div>

      <div className="flex flex-col md:flex-row gap-4 mb-6">
        <input
          type="text"
          value={searchTerm}
          onChange={handleSearch}
          placeholder="Search by package or skill name..."
          className="px-3 py-2 border rounded w-full md:w-1/2 dark:bg-gray-800 dark:text-white"
        />
        <select
          value={sortOption}
          onChange={handleSort}
          className="px-3 py-2 border rounded w-full md:w-1/3 dark:bg-gray-800 dark:text-white"
        >
          <option value="">Sort by</option>
          <option value="name-asc">Name (A–Z)</option>
          <option value="name-desc">Name (Z–A)</option>
          <option value="date-desc">Newest First</option>
          <option value="date-asc">Oldest First</option>
        </select>
      </div>

      {filteredPackages.length === 0 ? (
        <p className="text-gray-600 dark:text-gray-300">No packages found.</p>
      ) : (
        <div className="space-y-6">
          {filteredPackages.map((pkg) => (
            <div key={pkg.id} className="border rounded-lg shadow-sm bg-violet-50 dark:bg-violet-800">
              <div className="flex justify-between items-start p-4 border-b">
                <div>
                  <h2 className="text-xl font-semibold text-violet-900 dark:text-white">{pkg.title}</h2>
                  <p className="text-sm text-gray-700 dark:text-gray-300">Price: ₹{pkg.price}</p>
                  <p className="text-xs text-gray-500 dark:text-gray-400">Created: {new Date(pkg.createdAt).toLocaleString()}</p>
                </div>
                <div className="flex gap-4 mt-1">
                  <Link
                    to={`/package/edit/${pkg.id}`}
                    className="text-green-700 hover:underline dark:text-green-300 text-md"
                  >
                    Add new Skill for this package
                  </Link>
                  <button
                    onClick={() => handleDelete(pkg.id)}
                    className="text-red-600 hover:underline dark:text-red-300 text-md"
                  >
                    Delete
                  </button>
                </div>
              </div>

              <div className="p-4">
                <h3 className="text-md font-semibold mb-2 text-violet-700 dark:text-white">Skills</h3>
                {pkg.skills.length === 0 ? (
                  <p className="text-sm italic text-gray-600 dark:text-gray-400">No skills added yet.</p>
                ) : (
                  <div className="space-y-4">
                    {pkg.skills.map((skill) => (
                      <div
                        key={skill.id}
                        className="border-l-4 border-blue-500 pl-4 bg-white dark:bg-gray-800 p-3 rounded shadow-sm"
                      >
                        <div className="flex justify-between items-start">
                          <div>
                            <h4 className="font-semibold text-gray-900 dark:text-white">{skill.title}</h4>
                            <p className="text-sm text-gray-700 dark:text-gray-300">{skill.description}</p>
                          </div>
                          <div className="flex flex-col gap-2 text-sm text-center">
                            <Link
                              to={`/topic/add/${skill.id}`}
                              className="bg-teal-600 hover:bg-teal-700 text-white px-3 py-1 rounded"
                            >
                              + Add Topics and Sub Topics 
                            </Link>
                            <Link
                              to={`/skill/${skill.id}/topics`}
                              className="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded"
                            >
                              Upload Content
                            </Link>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PackageList;
