// components/Skill/SkillTopicsTable.jsx
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getSkillWithTopics } from '../../services/skillService';
import toast from 'react-hot-toast';
import mcqAttemptService from '../../services/mcqAttemptService';
import { format } from 'date-fns';

const McqTestResults = () => {
  const { skillId } = useParams();
  const [topics, setTopics] = useState([]);
  const [attemptMap, setAttemptMap] = useState({});
  const [expandedSubTopicId, setExpandedSubTopicId] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!skillId) return;

    const fetchData = async () => {
      try {
        const response = await getSkillWithTopics(skillId);
        const fetchedTopics = response.data.data.topics || [];
        setTopics(fetchedTopics);

        // Get all subTopic IDs
        const subTopicIds = fetchedTopics.flatMap(t =>
          t.subTopics?.map(st => st.id) || []
        );

        // Fetch all attempts in one go
        if (subTopicIds.length > 0) {
          const attemptResponse = await mcqAttemptService.getAttemptsByMultipleSubTopicIds(subTopicIds);
          console.log(attemptResponse)
          // Organize attempts by subTopicId
          const grouped = {};
          attemptResponse.data.forEach(a => {
            if (!grouped[a.subTopicId]) grouped[a.subTopicId] = [];
            grouped[a.subTopicId].push(a);
          });
          setAttemptMap(grouped);
        }
      } catch (error) {
        toast.error('Failed to fetch topics or attempts');
        console.log("error fetching attemtpts ", error)
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [skillId]);

  const toggleSubTopic = (id) => {
    setExpandedSubTopicId(prev => (prev === id ? null : id));
  };

  if (loading) return <p className="p-4">Loading...</p>;

  return (
    <div className="p-4">
      <h2 className="text-xl font-semibold mb-4">Topics & SubTopics</h2>
      <table className="w-full table-auto border border-gray-300 text-sm">
        <thead className="bg-gray-100">
          <tr>
            <th className="border px-4 py-2">Topic Name</th>
            <th className="border px-4 py-2">SubTopics</th>
          </tr>
        </thead>
        <tbody>
          {topics.map((topic) => (
            <tr key={topic.id}>
              <td className="border px-4 py-2 font-medium align-top">{topic.title}</td>
              <td className="border px-4 py-2">
                <ul className="list-disc ml-5">
                  {topic.subTopics.map((sub) => (
                    <li key={sub.id} className="mb-2">
                      <div className="flex items-center justify-between">
                        <span className="font-medium">{sub.title}</span>
                        <button
                          className="ml-4 text-blue-600 text-sm underline"
                          onClick={() => toggleSubTopic(sub.id)}
                        >
                          {expandedSubTopicId === sub.id ? 'Hide Attempts' : 'View Attempts'}
                        </button>
                      </div>

                      {expandedSubTopicId === sub.id && (
                        <div className="mt-2">
                          {attemptMap[sub.id]?.length ? (
                            <table className="w-full border mt-2 text-xs">
                              <thead className="bg-gray-200">
                                <tr>
                                  <th className="p-2 border">#</th>
                                  <th className="p-2 border">Attempt #</th>
                                  <th className="p-2 border">Score</th>
                                  <th className="p-2 border">Total Marks</th>
                                  <th className="p-2 border">Attempted At</th>
                                </tr>
                              </thead>
                              <tbody>
                                {attemptMap[sub.id].map((a, index) => (
                                  <tr key={a.id} className="text-center">
                                    <td className="p-1 border">{index + 1}</td>
                                    <td className="p-1 border">{a.attemptNumber}</td>
                                    <td className="p-1 border">{a.score}</td>
                                    <td className="p-1 border">{a.totalMarks}</td>
                                    <td className="p-1 border">{format(new Date(a.attemptedAt), 'dd MMM yyyy, hh:mm a')}</td>
                                  </tr>
                                ))}
                              </tbody>
                            </table>
                          ) : (
                            <p className="text-gray-500 mt-1">No attempts found</p>
                          )}
                        </div>
                      )}
                    </li>
                  ))}
                </ul>
              </td>
            </tr>
          ))}
          {topics.length === 0 && (
            <tr>
              <td colSpan="2" className="text-center py-4 text-gray-500">
                No topics found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default McqTestResults;

