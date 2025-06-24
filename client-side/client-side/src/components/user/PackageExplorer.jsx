import React, { useEffect, useState } from 'react';
import { getAllPackages } from '../../services/packageService';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';
import TopicViewerModal from '../topic/TopicViewerModel';
import reviewService from '../../services/reviewService';
import ReviewListModal from '../review/ReviewListModal';
import SearchAndSortBar from '../packages/SearchAndSortBar';

const PackageExplorer = () => {
  const [packages, setPackages] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortOption, setSortOption] = useState('');
  const [loading, setLoading] = useState(true);
  const [selectedTopics, setSelectedTopics] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [currentSkillTitle, setCurrentSkillTitle] = useState('');
  const [reviewSummaries, setReviewSummaries] = useState({});
  const [showReviewModal, setShowReviewModal] = useState(false);
  const [reviewTarget, setReviewTarget] = useState({ targetType: "", targetId: null });

  const navigate = useNavigate();

  const handleViewTopics = (skill) => {
    setSelectedTopics(skill.topics || []);
    setCurrentSkillTitle(skill.title);
    setShowModal(true);
  };

  const openReviewModal = (type, id) => {
    setReviewTarget({ targetType: type, targetId: id });
    setShowReviewModal(true);
  };

  useEffect(() => {
    const fetchPackages = async () => {
      try {
        const { data } = await getAllPackages();
        const packagesData = data.data;
        setPackages(packagesData);

        // Fetch review summaries for packages and skills
        const summaries = {};
        await Promise.all(
          packagesData.flatMap(async (pkg) => {
            const packageReviews = reviewService.getReviewSummary("PACKAGE", pkg.id);
            const skillReviews = pkg.skills?.map((skill) =>
              reviewService.getReviewSummary("SKILL", skill.id)
            ) || [];

            const all = [packageReviews, ...skillReviews];
            await Promise.allSettled(all.map(async (promise, i) => {
              try {
                const { data } = await promise;
                if (i === 0) summaries[pkg.id] = data;
                else summaries[`skill-${pkg.skills[i - 1].id}`] = data;
              } catch (err) {
                console.error("Review summary fetch error", err);
              }
            }));
          })
        );
        setReviewSummaries(summaries);
      } catch (err) {
        toast.error("Failed to fetch packages");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchPackages();
  }, []);

  const handleSort = (e) => {
    setSortOption(e.target.value);
  };

  const filteredAndSortedPackages = [...packages]
    .filter(pkg =>
      pkg.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      pkg.skills.some(skill => skill.title.toLowerCase().includes(searchTerm.toLowerCase()))
    )
    .sort((a, b) => {
      if (sortOption === 'title-asc') return a.title.localeCompare(b.title);
      if (sortOption === 'title-desc') return b.title.localeCompare(a.title);
      if (sortOption === 'date-newest') return new Date(b.createdAt) - new Date(a.createdAt);
      if (sortOption === 'date-oldest') return new Date(a.createdAt) - new Date(b.createdAt);
      return 0;
    });

  if (loading) return <div className="text-center mt-6 text-gray-500 dark:text-gray-300">Loading...</div>;

  return (
    <div className="p-6 max-w-7xl mx-auto bg-gray-100 dark:bg-gray-900 min-h-screen">
      <h1 className="text-4xl font-bold mb-8 text-center text-indigo-700 dark:text-indigo-400">
        Explore Learning Packages
      </h1>

      {/* Search + Sort */}
      {/* <div className="flex flex-col md:flex-row gap-4 justify-center mb-10">
        <input
          type="text"
          placeholder="Search by package or skill name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full md:w-1/2 p-3 border border-gray-300 dark:border-gray-600 rounded-lg shadow-sm 
                     bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 
                     focus:outline-none focus:ring-2 focus:ring-indigo-400 dark:focus:ring-indigo-500"
        />
        <select
          value={sortOption}
          onChange={handleSort}
          className="w-full md:w-1/3 p-3 border border-gray-300 dark:border-gray-600 rounded-lg shadow-sm 
                     bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100"
        >
          <option value="">Sort by</option>
          <option value="title-asc">Name (A–Z)</option>
          <option value="title-desc">Name (Z–A)</option>
          <option value="date-newest">Newest First</option>
          <option value="date-oldest">Oldest First</option>
        </select>
      </div> */}
      <SearchAndSortBar
        searchTerm={searchTerm}
        onSearchChange={setSearchTerm}
        sortOption={sortOption}
        onSortChange={setSortOption}
        placeholder="Search your packages or skills..."
      />


      {filteredAndSortedPackages.length === 0 ? (
        <p className="text-center text-gray-500 dark:text-gray-400">No packages found.</p>
      ) : (
        filteredAndSortedPackages.map((pkg) => (
          <div key={pkg.id} className="mb-12 border rounded-2xl p-6 dark:bg-gray-800">
            <h2 className="text-2xl font-bold text-indigo-800 dark:text-indigo-400 mb-2">{pkg.title}</h2>
            {reviewSummaries[pkg.id] && (
              <div className="text-sm text-yellow-600 dark:text-yellow-400 mb-2">
                ⭐ {reviewSummaries[pkg.id].averageRating.toFixed(1)} ({reviewSummaries[pkg.id].totalReviews} reviews)
              </div>
            )}
            {reviewSummaries[pkg.id] && (
              <button
                onClick={() => openReviewModal("PACKAGE", pkg.id)}
                className="text-sm text-indigo-600 hover:underline mb-2"
              >
                View All Reviews
              </button>
            )}
            <p className="text-gray-700 dark:text-gray-300 mb-2">{pkg.description}</p>
            <p className="text-sm text-gray-500 dark:text-gray-400 mb-4">Package Price: ₹{pkg.price}</p>

            {pkg.skills?.length === 0 ? (
              <p className="text-gray-500 dark:text-gray-400 italic">No skills added to this package yet.</p>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
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
                    <p className="text-sm text-gray-500 dark:text-gray-400">
                      Topics: <strong>{skill.topics?.length || 0}</strong>
                    </p>
                    {reviewSummaries[`skill-${skill.id}`] && (
                      <div className="text-sm text-yellow-600 dark:text-yellow-400 mb-1">
                        ⭐ {reviewSummaries[`skill-${skill.id}`].averageRating.toFixed(1)} ({reviewSummaries[`skill-${skill.id}`].totalReviews} reviews)
                      </div>
                    )}

                    {reviewSummaries[`skill-${skill.id}`] && (
                      <button
                        onClick={() => openReviewModal("SKILL", skill.id)}
                        className="text-xs text-indigo-600 hover:underline mb-2"
                      >
                        View All Reviews
                      </button>
                    )}

                    <button
                      onClick={() => handleViewTopics(skill)}
                      className="mt-3 w-full bg-indigo-600 text-white py-2 px-4 rounded 
                   hover:bg-indigo-700 dark:hover:bg-indigo-500 text-sm"
                    >
                      View Topics
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))
      )}

      {/* Modals */}
      <TopicViewerModal
        visible={showModal}
        onClose={() => setShowModal(false)}
        skillTitle={currentSkillTitle}
        topics={selectedTopics}
      />
      <ReviewListModal
        isOpen={showReviewModal}
        onClose={() => setShowReviewModal(false)}
        targetType={reviewTarget.targetType}
        targetId={reviewTarget.targetId}
      />
    </div>
  );
};

export default PackageExplorer;
