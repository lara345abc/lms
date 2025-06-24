import React from 'react';

const TopicViewerModal = ({ visible, onClose, skillTitle, topics }) => {
  if (!visible) return null;

  return (
    <div className="fixed inset-0 z-50 bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg w-full max-w-2xl max-h-[80vh] overflow-y-auto shadow-lg">
        <h2 className="text-xl font-bold mb-4 text-indigo-700 dark:text-indigo-300">{skillTitle} - Topics</h2>

        <div className="space-y-4">
          {topics.map((topic) => (
            <div key={topic.id}>
              <div className="flex items-start gap-2">
                <span className="text-indigo-500">•</span>
                <div>
                  <h3 className="text-md font-semibold text-gray-800 dark:text-gray-200">{topic.title || 'Untitled Topic'}</h3>
                  <ul className="ml-6 mt-1 list-disc text-sm text-gray-700 dark:text-gray-300">
                    {topic.subTopics.map((sub) => (
                      <li key={sub.id}>{sub.title || 'Untitled Subtopic'}</li>
                    ))}
                  </ul>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="mt-6 flex justify-end">
          <button
            className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
            onClick={onClose}
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default TopicViewerModal;
