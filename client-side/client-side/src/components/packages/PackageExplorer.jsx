// components/Explore/PackageExplorer.jsx
import React, { useEffect, useState } from 'react';
import { getAllPackages } from '../../services/packageService';
import toast from 'react-hot-toast';

const PackageExplorer = () => {
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchPackages = async () => {
      try {
        const { data } = await getAllPackages();
        setPackages(data.data);
      } catch (error) {
        toast.error('Failed to fetch packages');
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchPackages();
  }, []);

  if (loading) return <div className="text-center mt-6 text-gray-500">Loading...</div>;

  return (
    <div className="p-4 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-6 text-center">Explore Learning Packages</h1>
      {packages.map((pkg) => (
        <div key={pkg.id} className="mb-8 border rounded-lg p-4 bg-white shadow">
          <h2 className="text-2xl font-semibold text-indigo-700">{pkg.title}</h2>
          <p className="text-gray-700 mb-2">{pkg.description}</p>
          <p className="text-sm text-gray-500 mb-4">Price: ₹{pkg.price}</p>

          {pkg.skills.map((skill) => (
            <div key={skill.id} className="ml-4 border-l-2 border-indigo-300 pl-4 mb-4">
              <h3 className="text-xl font-semibold text-indigo-600">{skill.title}</h3>
              <p className="text-gray-600 mb-2">{skill.description}</p>
              <p className="text-sm text-gray-500 mb-2">Skill Price: ₹{skill.price}</p>

              {skill.topics && skill.topics.length > 0 && skill.topics.map((topic) => (
                <div key={topic.id} className="ml-4 border-l-2 border-green-300 pl-4 mb-4">
                  <h4 className="text-lg font-medium text-green-700">{topic.title}</h4>
                  <p className="text-gray-600 mb-2">{topic.description}</p>

                  {topic.subTopics && topic.subTopics.length > 0 && topic.subTopics.map((sub) => (
                    <div key={sub.id} className="ml-4 border-l-2 border-blue-200 pl-4 mb-2">
                      <h5 className="text-md font-semibold text-blue-800">{sub.title}</h5>
                      <p className="text-gray-600 text-sm">{sub.description}</p>

                      {/* Videos */}
                      {sub.videos && sub.videos.length > 0 && (
                        <div className="mt-2">
                          <p className="font-medium text-sm mb-1 text-gray-700">Videos:</p>
                          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                            {sub.videos.map((video) => (
                              <div key={video.id} className="border p-2 rounded bg-gray-100">
                                <video src={video.url} controls className="w-full h-40 object-cover rounded" poster={video.thumbnailUrl} />
                              </div>
                            ))}
                          </div>
                        </div>
                      )}

                      {/* Study Materials */}
                      {sub.studyMaterials && sub.studyMaterials.length > 0 && (
                        <div className="mt-2">
                          <p className="font-medium text-sm text-gray-700 mb-1">Study Materials:</p>
                          <ul className="list-disc list-inside text-sm text-blue-700">
                            {sub.studyMaterials.map((mat) => (
                              <li key={mat.id}>
                                <a href={mat.pdfUrl} target="_blank" rel="noopener noreferrer" className="underline">
                                  Download PDF
                                </a>
                              </li>
                            ))}
                          </ul>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              ))}
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

export default PackageExplorer;
