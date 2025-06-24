import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getSubTopicById } from '../../services/packageService';
import toast from 'react-hot-toast';

const SubTopicDetails = () => {
  const { id } = useParams();
  const [subTopic, setSubTopic] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchSubTopic = async () => {
    try {
      const { data } = await getSubTopicById(id);
      setSubTopic(data.data);
    } catch (err) {
      console.error(err);
      toast.error('Failed to fetch subtopic');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSubTopic();
  }, [id]);

  if (loading) return <div className="text-center mt-8 text-gray-500">Loading...</div>;
  if (!subTopic) return null;

  return (
    <div className="p-4 max-w-4xl mx-auto bg-white dark:bg-gray-900 text-gray-800 dark:text-white">
      <h1 className="text-2xl font-bold mb-4">{subTopic.title}</h1>
      <p className="mb-4 text-gray-600 dark:text-gray-300">{subTopic.description}</p>

      <h2 className="text-lg font-semibold mb-2">Videos</h2>
      {subTopic.videos.length === 0 ? (
        <p className="italic text-gray-500">No videos available.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {subTopic.videos.map((video) => (
            <div key={video.id} className="border p-2 rounded bg-white dark:bg-gray-800">
              <video
                src={video.url}
                controls
                className="w-full h-40 object-cover rounded"
                poster={video.thumbnailUrl}
              />
              <p className="text-xs mt-1 text-gray-600 dark:text-gray-300">Views: {video.noOfViews}</p>
            </div>
          ))}
        </div>
      )}

      <h2 className="text-lg font-semibold mt-6 mb-2">Study Materials</h2>
      {subTopic.studyMaterials.length === 0 ? (
        <p className="italic text-gray-500">No study materials available.</p>
      ) : (
        <ul className="list-disc list-inside">
          {subTopic.studyMaterials.map((mat) => (
            <li key={mat.id}>
              <a href={mat.url} target="_blank" rel="noopener noreferrer" className="text-blue-600 underline">
                {mat.title}
              </a>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default SubTopicDetails;
