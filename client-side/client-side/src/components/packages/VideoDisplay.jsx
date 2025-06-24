import { useNavigate, useParams } from "react-router-dom";
import ReviewModal from "../review/ReviewModal";
import { useEffect, useState } from "react";
import { getPackageById, downloadAllStudyMaterials } from "../../services/packageService";
import ReactPlayer from "react-player";
import { FaEdit, FaPen, FaDownload } from "react-icons/fa"; // Import FaDownload icon for download button
import toast from 'react-hot-toast';
import CommentModal from "../comment/CommentModal";
import VideoCommentsSection from "../comment/VideoCommentSection";
import DownloadTemplateButton from "../mcqs/DownloadTemplateButton";

const VideoDisplay = () => {
  const { id } = useParams();
  const [pkg, setPkg] = useState(null);
  const [selectedVideo, setSelectedVideo] = useState(null);
  const [selectedInfo, setSelectedInfo] = useState({ topicTitle: '', subTopicTitle: '' });
  const [selectedIds, setSelectedIds] = useState({ videoId: null, subTopicId: null, topicId: null });
  const [openTopicId, setOpenTopicId] = useState(null);
  const [modalInfo, setModalInfo] = useState({ isOpen: false, targetType: "", targetId: null });
  const [openSubTopicIds, setOpenSubTopicIds] = useState({});
  const [isCommentModalOpen, setIsCommentModalOpen] = useState(false);
  const navigate = useNavigate();
  let accumulatedSubTopicIds = [];


  useEffect(() => {
    const fetchPackage = async () => {
      try {
        const { data } = await getPackageById(id);
        setPkg(data.data);
        for (let skill of data.data.skills) {
          for (let topic of skill.topics) {
            for (let sub of topic.subTopics) {
              if (sub.videos.length > 0) {
                setSelectedVideo(sub.videos[0].url);
                setSelectedInfo({ topicTitle: topic.title, subTopicTitle: sub.title });
                setSelectedIds({ videoId: sub.videos[0].id, subTopicId: sub.id, topicId: topic.id });
                return;
              }
            }
          }
        }
      } catch (err) {
        toast.error('Failed to load package');
        console.error(err);
      }
    };

    fetchPackage();
  }, [id]);

  const toggleTopic = (topicId) => {
    setOpenTopicId(prev => (prev === topicId ? null : topicId));
  };

  const toggleSubTopic = (subTopicId) => {
    setOpenSubTopicIds(prev => ({
      ...prev,
      [subTopicId]: !prev[subTopicId],
    }));
  };


  const openReviewModal = (type, targetId) => {
    setModalInfo({ isOpen: true, targetType: type.toUpperCase(), targetId });
  };

  const handleDownloadAll = async (subTopicId) => {
    console.log("subtopic id ", subTopicId)
    try {
      const response = await downloadAllStudyMaterials(subTopicId);
      const blob = new Blob([response.data], { type: 'application/zip' });
      const url = window.URL.createObjectURL(blob);

      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `resources-subtopic-${subTopicId}.zip`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Download failed', error);
      toast.error('Failed to download study materials');
    }
  };

  if (!pkg) return <div className="text-center mt-6 text-gray-500">Loading package...</div>;

  const allSubTopicIds = pkg.skills.flatMap(skill =>
    skill.topics.flatMap(topic => topic.subTopics.map(sub => sub.id))
  );


  return (
    <div className="p-4 md:p-6 max-w-screen-xl mx-auto">
      <h1 className="text-2xl md:text-3xl font-bold mb-2">{pkg.title}</h1>
      <button onClick={() => openReviewModal("package", pkg.id)} className="bg-indigo-600 text-white px-3 py-1 rounded text-sm">
        Review This Package <FaPen className="text-sm inline" />
      </button>
      <p className="text-gray-700 mb-4">{pkg.description}</p>

      <div className="flex flex-col lg:flex-row gap-6">
        {/* Video Player */}
        <div className="w-full lg:w-2/3 bg-black rounded-xl overflow-hidden shadow-md">
          {selectedVideo ? (
            <div className="aspect-video">
              <ReactPlayer url={selectedVideo} controls width="100%" height="100%" className="rounded-xl" />
            </div>
          ) : (
            <div className="text-white p-6">No video available</div>
          )}
          {selectedInfo.topicTitle && (
            <div className="p-3 text-white bg-gray-900 text-sm">
              <strong>{selectedInfo.topicTitle}</strong> → {selectedInfo.subTopicTitle}
            </div>
          )}
          <div className="flex flex-wrap gap-3 p-4 bg-gray-100 border-t text-sm">
            <button
              onClick={() => setIsCommentModalOpen(true)}
              className="bg-green-600 text-white px-3 py-1 rounded"
            >
              Leave a Comment
            </button>
          </div>
        </div>

        {/* Accordion Sidebar */}
        <div className="w-full lg:w-1/3 bg-white rounded-xl shadow p-4 overflow-auto max-h-[80vh]">
          {pkg.skills.map((skill) =>
            skill.topics.map((topic) => {
              const topicSubTopicIds = topic.subTopics.map((s) => s.id);
              const accumulatedSubTopicIds = [];

              return (
                <div key={topic.id} className="mb-3 border-b">
                  <button
                    className="w-full text-left font-semibold text-indigo-600 py-2 flex justify-between items-center"
                    onClick={() => toggleTopic(topic.id)}
                  >
                    <span>{topic.title}</span>
                    <span>{openTopicId === topic.id ? "▲" : "▼"}</span>
                  </button>

                  {openTopicId === topic.id &&
                    topic.subTopics.map((sub) => {
                      accumulatedSubTopicIds.push(sub.id);
                      return (
                        <div key={sub.id} className="ml-4 mb-2">
                          <button
                            onClick={() => toggleSubTopic(sub.id)}
                            className="w-full text-left text-sm font-medium text-gray-700 flex justify-between items-center"
                          >
                            {sub.title}
                            <span>{openSubTopicIds[sub.id] ? "▲" : "▼"}</span>
                          </button>

                          {openSubTopicIds[sub.id] && (
                            <>
                              <ul className="ml-4 mt-1 space-y-1">
                                {sub.videos.map((video, index) => (
                                  <li
                                    key={video.id}
                                    className="text-sm text-blue-600 cursor-pointer hover:underline"
                                    onClick={() => {
                                      setSelectedVideo(video.url);
                                      setSelectedInfo({ topicTitle: topic.title, subTopicTitle: sub.title });
                                      setSelectedIds({ videoId: video.id, subTopicId: sub.id, topicId: topic.id });
                                    }}
                                  >
                                    {video.title || `Video ${index + 1}`}
                                  </li>
                                ))}

                                <li>
                                  <button
                                    onClick={() => handleDownloadAll(sub.id)}
                                    className="bg-yellow-500 text-white text-xs px-2 py-1 rounded hover:bg-yellow-600"
                                  >
                                    Download PDF
                                  </button>
                                </li>

                                <li className="flex gap-2">
                                  <button
                                    onClick={() => navigate(`/mcq-test/${sub.id}`)}
                                    className="bg-blue-600 text-white text-xs px-2 py-1 rounded hover:bg-blue-700"
                                    title={`Take test for SubTopic: ${sub.title}`}
                                  >
                                    Take Test
                                  </button>
                                  <button
                                    onClick={() => navigate(`/mcq-test/${accumulatedSubTopicIds.join(",")}`)}
                                    className="bg-purple-600 text-white text-xs px-2 py-1 rounded hover:bg-purple-700"
                                    title="Take test for all SubTopics completed so far"
                                  >
                                    Cumulative Test
                                  </button>
                                </li>
                              </ul>
                            </>
                          )}
                        </div>
                      );
                    })}

                  {openTopicId === topic.id && topic.subTopics.length > 0 && (
                    <div className="ml-4 mt-2">
                      <button
                        onClick={() => navigate(`/mcq-test/${topicSubTopicIds.join(",")}`)}
                        className="bg-teal-600 text-white text-xs px-3 py-1 rounded hover:bg-teal-700"
                        title={`Take test for Topic: ${topic.title}`}
                      >
                        Take Test on All Topics
                      </button>
                    </div>
                  )}
                </div>
              );
            })
          )}

          {/* Button at the end for full test on all subtopics */}
          <div className="mt-6 text-center">
            <button
              onClick={() =>
                navigate(
                  `/mcq-test/${pkg.skills
                    .flatMap((skill) => skill.topics.flatMap((t) => t.subTopics.map((s) => s.id)))
                    .join(",")}`
                )
              }
              className="bg-black text-white text-sm px-4 py-2 rounded hover:bg-gray-800"
              title="Take test for all SubTopics"
            >
              Take Full Test
            </button>
          </div>
        </div>
      </div>

      {/* Modals & Comments */}
      <ReviewModal
        isOpen={modalInfo.isOpen}
        onClose={() => setModalInfo({ ...modalInfo, isOpen: false })}
        targetType={modalInfo.targetType}
        targetId={modalInfo.targetId}
      />
      <CommentModal
        isOpen={isCommentModalOpen}
        onClose={() => setIsCommentModalOpen(false)}
        videoId={selectedIds.videoId}
      />
      <VideoCommentsSection videoId={selectedIds.videoId} />
    </div>

  );
};

export default VideoDisplay;
