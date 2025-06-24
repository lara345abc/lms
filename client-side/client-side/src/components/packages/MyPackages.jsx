import React, { useEffect, useState } from 'react';
import userService from '../../services/userService';
import { FaSpinner } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import ReviewModal from '../review/ReviewModal';
import SearchAndSortBar from './SearchAndSortBar';

const MyPackages = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [modalInfo, setModalInfo] = useState({ isOpen: false, targetType: "", targetId: null });
  const [searchTerm, setSearchTerm] = useState('');
  const [sortOption, setSortOption] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await userService.getUserByEmail(); // Includes packages with skills
        setUser(response.data);
        console.log("User detals with packages ",response.data)
      } catch (error) {
        console.error('Failed to fetch user:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  const openReviewModal = (type, targetId) => {
    setModalInfo({ isOpen: true, targetType: type.toUpperCase(), targetId });
  };

  if (loading) {
    return (
      <div className="text-center p-10">
        <FaSpinner className="animate-spin text-5xl text-indigo-600 mx-auto" />
        <p className="mt-4 text-gray-500">Loading your packages...</p>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="text-center p-10 text-red-500">
        <p>Failed to load user information.</p>
      </div>
    );
  }

  let filteredPackages = [...(user.packages || [])]
    .filter(pkg =>
      pkg.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      pkg.skills?.some(skill => skill.title.toLowerCase().includes(searchTerm.toLowerCase()))
    )
    .sort((a, b) => {
      if (sortOption === 'title-asc') return a.title.localeCompare(b.title);
      if (sortOption === 'title-desc') return b.title.localeCompare(a.title);
      if (sortOption === 'date-newest') return new Date(b.createdAt) - new Date(a.createdAt);
      if (sortOption === 'date-oldest') return new Date(a.createdAt) - new Date(b.createdAt);
      return 0;
    });

  return (
    <div className="p-6 max-w-7xl mx-auto bg-gray-100 dark:bg-gray-900 min-h-screen">
      <h1 className="text-4xl font-bold mb-8 text-center text-indigo-700 dark:text-indigo-400">
        My Learning Packages
      </h1>

      {/* <div className="mb-6 flex flex-col md:flex-row justify-between items-center gap-4">
        <input
          type="text"
          placeholder="Search by package or skill title..."
          className="w-full md:w-1/2 p-3 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-white"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <select
          value={sortOption}
          onChange={(e) => setSortOption(e.target.value)}
          className="p-3 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-800 text-gray-900 dark:text-white"
        >
          <option value="">Sort By</option>
          <option value="title-asc">Package Title (A–Z)</option>
          <option value="title-desc">Package Title (Z–A)</option>
          <option value="date-newest">Created Date (Newest)</option>
          <option value="date-oldest">Created Date (Oldest)</option>
        </select>
      </div> */}

      <SearchAndSortBar
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        sortOption={sortOption}
        onSortChange={setSortOption}
        placeholder="Search your packages or skills..."
      />


      {filteredPackages.length > 0 ? (
        filteredPackages.map((pkg) => (
          <div key={pkg.id} className="mb-12 border rounded-2xl p-6 dark:bg-gray-800">
            <h2 className="text-2xl font-bold text-indigo-800 dark:text-indigo-400 mb-2">{pkg.title}</h2>
            <p className="text-gray-700 dark:text-gray-300 mb-2">{pkg.description}</p>
            <p className="text-sm text-gray-500 dark:text-gray-400 mb-4">Package Price: ₹{pkg.price}</p>

            <button
              onClick={() => openReviewModal("package", pkg.id)}
              className="bg-indigo-600 text-white py-2 px-4 rounded hover:bg-indigo-700 dark:hover:bg-indigo-500 text-sm flex items-center gap-2"
            >
              📝 Give Feedback on Package
            </button>

            {pkg.skills?.length === 0 ? (
              <p className="text-gray-500 dark:text-gray-400 italic">No skills added to this package yet.</p>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mt-4">
                {pkg.skills.map((skill) => (
                  <div
                    key={skill.id}
                    className="bg-gray-50 dark:bg-gray-700 rounded-xl shadow-sm p-4 border 
                 border-gray-200 dark:border-gray-600 
                 hover:border-indigo-500 dark:hover:border-indigo-400 
                 hover:shadow-md transition-all duration-200"
                  >
                    <h3 className="text-lg font-semibold text-indigo-600 dark:text-indigo-300">{skill.title}</h3>
                    <p className="text-gray-600 dark:text-gray-300 mb-1 text-sm">{skill.description}</p>
                    <p className="text-sm text-gray-500 dark:text-gray-400">Skill Price: ₹{skill.price}</p>
                    {/* <p className="text-sm text-gray-500 dark:text-gray-400">
                      Topics: <strong>{skill.topics?.length || 0}</strong>
                    </p> */}

                    <button
                      onClick={() => navigate(`/topics/${pkg.id}`)}
                      className="mt-3 w-full bg-blue-600 text-white py-2 px-4 rounded 
                   hover:bg-indigo-700 dark:hover:bg-indigo-500 text-sm"
                    >
                      📚 Start Learning 
                    </button>
                    <button
                      onClick={() => navigate(`/mcq-test/resutls/${skill.id}`)}
                      className="mt-3 w-full bg-indigo-600 text-white py-2 px-4 rounded 
                   hover:bg-indigo-700 dark:hover:bg-indigo-500 text-sm"
                    >
                      📚 MCQ Results
                    </button>

                    <button
                      onClick={() => openReviewModal("skill", skill.id)}
                      className="mt-2 w-full bg-gray-800 text-white py-2 px-4 rounded 
                   hover:bg-gray-900 dark:hover:bg-gray-600 text-sm flex items-center justify-center gap-2"
                    >
                      📝 Give Feedback on Skill
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))
      ) : (
        <p className="text-center text-gray-500 text-lg dark:text-gray-400">
          No matching packages found.
        </p>
      )}

      <ReviewModal
        isOpen={modalInfo.isOpen}
        onClose={() => setModalInfo({ ...modalInfo, isOpen: false })}
        targetType={modalInfo.targetType}
        targetId={modalInfo.targetId}
      />
    </div>
  );
};

export default MyPackages;
