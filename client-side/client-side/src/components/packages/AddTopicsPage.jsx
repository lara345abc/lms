import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import topicService from '../../services/topicService';
import toast from 'react-hot-toast';

const AddTopicsPage = () => {
  const { skillId } = useParams();
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchSkillWithTopics = async () => {
      try {
        const res = await topicService.getTopicsBySkillId(skillId);
        const topicData = res.data.map(topic => ({
          ...topic,
          subTopics: topic.subTopics || [],
        }));
        setTopics(topicData);
      } catch (err) {
        toast.error('Failed to fetch topics');
      } finally {
        setLoading(false);
      }
    };
    fetchSkillWithTopics();
  }, [skillId]);

  const handleTopicChange = (index, field, value) => {
    const updated = [...topics];
    updated[index][field] = value;
    setTopics(updated);
  };

  const handleSubTopicChange = (topicIdx, subIdx, field, value) => {
    const updated = [...topics];
    updated[topicIdx].subTopics[subIdx][field] = value;
    setTopics(updated);
  };

  const addTopic = () => {
    setTopics([
      ...topics,
      { title: '', description: '', subTopics: [{ title: '', description: '' }] },
    ]);
  };

  const addSubTopic = (topicIdx) => {
    const updated = [...topics];
    updated[topicIdx].subTopics.push({ title: '', description: '' });
    setTopics(updated);
  };

  const deleteTopic = async (index, id) => {
    if (id) {
      await topicService.deleteTopic(id);
      toast.success('Topic deleted');
    }
    setTopics(topics.filter((_, i) => i !== index));
  };

  const deleteSubTopic = async (topicIdx, subIdx, id) => {
    if (id) {
      await topicService.deleteSubTopic(id);
      toast.success('SubTopic deleted');
    }
    const updated = [...topics];
    updated[topicIdx].subTopics.splice(subIdx, 1);
    setTopics(updated);
  };

  const handleSubmit = async () => {
    try {
      for (const topic of topics) {
        if (topic.id) {
          await topicService.updateTopic(topic.id, {
            title: topic.title,
            description: topic.description,
            skillId: parseInt(skillId),
          });
        } else {
          const created = await topicService.createTopic({
            skillId: parseInt(skillId),
            title: topic.title,
            description: topic.description,
            subTopics: topic.subTopics,
          });
          continue; // skip subtopics, already handled
        }

        for (const sub of topic.subTopics) {
          if (sub.id) {
            await topicService.updateSubTopic(sub.id, {
              title: sub.title,
              description: sub.description,
              topicId: topic.id,
            });
          } else {
            await topicService.createSubTopic({
              title: sub.title,
              description: sub.description,
              topicId: topic.id,
            });
          }
        }
      }
      toast.success('Topics & Subtopics saved!');
    } catch (error) {
      console.error(error);
      toast.error('Failed to save topics');
    }
  };
  // const handleViewQuestions = () => {
  //       navigate(`/subtopic-mcqs/${subTopicId}`);
  //   };

  if (loading) return <div className="p-4">Loading...</div>;

  return (
    <div className="p-4 max-w-4xl mx-auto bg-white dark:bg-gray-900 text-gray-800 dark:text-white">
      <h1 className="text-2xl font-bold mb-4">Add / Edit Topics & Subtopics</h1>

      {topics.map((topic, i) => (
        <div key={i} className="mb-6 border p-4 rounded-lg bg-gray-100 dark:bg-gray-800">
          <input
            placeholder="Topic Title"
            value={topic.title}
            onChange={(e) => handleTopicChange(i, 'title', e.target.value)}
            className="w-full p-2 mb-2 rounded"
          />
          <textarea
            placeholder="Topic Description"
            value={topic.description}
            onChange={(e) => handleTopicChange(i, 'description', e.target.value)}
            className="w-full p-2 mb-2 rounded"
          />

          <div className="flex justify-between items-center mb-2">
            <h3 className="text-lg font-semibold">SubTopics</h3>
            <button
              onClick={() => deleteTopic(i, topic.id)}
              className="text-red-600 text-sm"
            >
              Delete Topic
            </button>
          </div>

          {topic.subTopics.map((sub, j) => (
            <div key={j} className="mb-2 border-l-4 pl-2">
              <input
                placeholder="SubTopic Title"
                value={sub.title}
                onChange={(e) => handleSubTopicChange(i, j, 'title', e.target.value)}
                className="w-full p-2 mb-1 rounded"
              />
              <textarea
                placeholder="SubTopic Description"
                value={sub.description}
                onChange={(e) => handleSubTopicChange(i, j, 'description', e.target.value)}
                className="w-full p-2 mb-2 rounded"
              />
              <button
                onClick={() => deleteSubTopic(i, j, sub.id)}
                className="text-xs text-white rounded  px-3 py-1 bg-red-500"
              >
                Delete SubTopic
              </button>

              <button
                onClick={() => navigate(`/subtopic-mcqs/${sub.id}`)}
                className="bg-gray-600 text-white mx-4 px-3 py-1 rounded text-xs hover:bg-gray-700"
              >
                View Added Questions
              </button>
            </div>
          ))}

          <button
            onClick={() => addSubTopic(i)}
            className="bg-blue-500 text-white px-3 py-1 rounded text-sm mt-2"
          >
            + Add SubTopic
          </button>
        </div>
      ))}

      <button onClick={addTopic} className="bg-green-600 text-white px-4 py-2 rounded mr-2">
        + Add Topic
      </button>
      <button onClick={handleSubmit} className="bg-indigo-600 text-white px-4 py-2 rounded">
        Save All
      </button>
    </div>
  );
};

export default AddTopicsPage;
