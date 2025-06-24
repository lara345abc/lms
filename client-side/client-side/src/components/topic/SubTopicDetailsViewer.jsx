import React, { useEffect, useState } from 'react';
import topicService from '../../services/topicService';
import toast from 'react-hot-toast';

const SubTopicDetailsViewer = ({ subTopicIds }) => {
  const [subTopics, setSubTopics] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!subTopicIds || subTopicIds.length === 0) return;

    const fetchSubTopics = async () => {
      try {
        const response = await topicService.getByIds(subTopicIds);
        setSubTopics(response.data || []);
      } catch (err) {
        toast.error('Failed to fetch subtopic details');
        console.log(err)
      } finally {
        setLoading(false);
      }
    };

    fetchSubTopics();
  }, [subTopicIds]);

  if (loading) return <div className="p-4">Loading subtopic details...</div>;

  return (
    <div className="flex flex-wrap gap-4">
      {subTopics.map((sub) => (
        <div key={sub.id} className="px-3 py-2 bg-white dark:bg-gray-800 border rounded shadow-sm">
          <h3 className="text-sm font-semibold text-indigo-700">{sub.title}</h3>
        </div>
      ))}
    </div>

  );
};

export default SubTopicDetailsViewer;
