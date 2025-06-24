import { useNavigate, useParams } from 'react-router-dom';
import { uploadVideoAndThumbnail, getTopicsBySkillId, uploadStudyMaterial, downloadAllStudyMaterials } from '../../services/packageService';
import toast from 'react-hot-toast';
import { useEffect, useState } from 'react';
import VideoPlayerModal from './VideoPlayerModal';
import McqUploadForm from '../mcqs/McqUploadForm';
import McqUploadWithSubTopicForm from '../mcqs/McqUploadWithSubTopicForm';

const ViewTopicsPage = () => {
    const { skillId } = useParams();
    const navigate = useNavigate();
    const [topics, setTopics] = useState([]);
    const [loading, setLoading] = useState(true);
    const [fileInputs, setFileInputs] = useState({}); // { [subTopicId]: { video: File, thumbnail: File } }
    const [uploadProgress, setUploadProgress] = useState({}); // { [subTopicId]: percent }
    const [modalVideoUrl, setModalVideoUrl] = useState(null);


    const fetchTopics = async () => {
        try {
            const { data } = await getTopicsBySkillId(skillId);
            setTopics(data.data);
            console.log("subtopic details :::::", data.data)
        } catch (err) {
            console.error(err);
            toast.error('Failed to fetch topics');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTopics();
    }, [skillId]);

    const handleFileChange = (e, subTopicId, type) => {
        const file = e.target.files[0];
        setFileInputs((prev) => ({
            ...prev,
            [subTopicId]: {
                ...prev[subTopicId],
                [type]: file,
            },
        }));
    };


    const handleUpload = async (subTopicId) => {
        console.log("subtopic id ", subTopicId)
        const inputs = fileInputs[subTopicId];
        if (!inputs?.video || !inputs?.thumbnail) {
            toast.error('Please select both video and thumbnail');
            return;
        }

        const formData = new FormData();
        formData.append('video', inputs.video);
        formData.append('thumbnail', inputs.thumbnail);

        try {
            await uploadVideoAndThumbnail(subTopicId, formData, (event) => {
                const percent = Math.round((event.loaded * 100) / event.total);
                setUploadProgress((prev) => ({ ...prev, [subTopicId]: percent }));
            });
            toast.success('Uploaded successfully');
            setUploadProgress((prev) => ({ ...prev, [subTopicId]: 0 }));
            fetchTopics(); // Refresh
        } catch (err) {
            console.error(err);
            toast.error('Upload failed');
            setUploadProgress((prev) => ({ ...prev, [subTopicId]: 0 }));
        }
    };

    const handlePdfUpload = async (subTopicId) => {
        const inputs = fileInputs[subTopicId];
        if (!inputs?.pdf) {
            toast.error('Please select a PDF file');
            return;
        }
        if (!inputs?.name?.trim()) {
            toast.error('Please enter a name');
            return;
        }

        const formData = new FormData();
        formData.append('subTopicId', subTopicId);
        formData.append('name', inputs.name);
        formData.append('pdf', inputs.pdf);

        try {
            await uploadStudyMaterial(subTopicId, formData);
            toast.success('PDF uploaded successfully');
            fetchTopics(); // Refresh
        } catch (err) {
            console.error(err);
            toast.error('PDF upload failed');
        }
    };

    const handleDownloadAll = async (subTopicId) => {
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




    if (loading) return <div className="text-center mt-8 text-gray-500">Loading...</div>;

    return (
        <div className="p-4 max-w-6xl mx-auto bg-white dark:bg-gray-900 text-gray-800 dark:text-white">
            <h1 className="text-2xl font-bold mb-6">Skill Name : {topics[0]?.skillTitle || 'Skill Name'}</h1>
            <McqUploadWithSubTopicForm />
            {topics.length === 0 ? (
                <p className="text-gray-600 dark:text-gray-400">No topics found for this skill.</p>
            ) : (
                topics.map((topic) => (
                    <div key={topic.id} className="mb-8 border p-4 rounded-lg bg-gray-100 dark:bg-gray-800">
                        <h2 className="text-xl font-semibold mb-1">Topic: {topic.title}</h2>
                        <p className="text-sm text-gray-700 dark:text-gray-300 mb-4">{topic.description}</p>

                        {topic.subTopics.length === 0 ? (
                            <p className="text-gray-600 dark:text-gray-400 italic">No subtopics added.</p>
                        ) : (
                            <div className="space-y-6">
                                {topic.subTopics.map((sub) => (
                                    <div key={sub.id} className="overflow-x-auto shadow rounded bg-white dark:bg-gray-800">
                                        <div className='text-center'>
                                            <div className="font-bold text-2xl">{sub.title}</div>
                                            <p className="text-sm text-gray-600 dark:text-gray-300">sub topic id :  {sub.id}</p>
                                            <p className="text-sm text-gray-600 dark:text-gray-300">{sub.description}</p>
                                        </div>
                                        <table className="min-w-full text-sm text-left">

                                            <tbody className="text-gray-900 dark:text-white">
                                                <tr className="border-t dark:border-gray-600">

                                                    <td className="px-4 py-2 space-y-6">
                                                        <div>
                                                            <h4 className="text-md font-semibold mb-2">🎥 Video & Thumbnail Upload</h4>
                                                            <div className="grid grid-cols-3 gap-4 items-center text-sm mb-2">
                                                                <label className="font-medium">Select Video:</label>
                                                                <input
                                                                    type="file"
                                                                    accept="video/*"
                                                                    onChange={(e) => handleFileChange(e, sub.id, 'video')}
                                                                    className="col-span-2"
                                                                />

                                                                <label className="font-medium">Select Thumbnail:</label>
                                                                <input
                                                                    type="file"
                                                                    accept="image/*"
                                                                    onChange={(e) => handleFileChange(e, sub.id, 'thumbnail')}
                                                                    className="col-span-2"
                                                                />

                                                                <span></span>
                                                                <button
                                                                    onClick={() => handleUpload(sub.id)}
                                                                    className="col-span-2 bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                                                                >
                                                                    Upload Video & Thumbnail
                                                                </button>
                                                            </div>

                                                            {uploadProgress[sub.id] > 0 && (
                                                                <div className="w-full bg-gray-200 rounded h-2 mt-1">
                                                                    <div
                                                                        className="bg-green-500 h-2 rounded transition-all duration-300"
                                                                        style={{ width: `${uploadProgress[sub.id]}%` }}
                                                                    ></div>
                                                                </div>
                                                            )}
                                                        </div>


                                                        {/* PDF Upload */}
                                                        <div>
                                                            <h4 className="text-md font-semibold mb-2">📄 PDF Upload</h4>
                                                            <div className="grid grid-cols-3 gap-4 items-center text-sm mb-2">
                                                                <label className="font-medium">PDF Name:</label>
                                                                <input
                                                                    type="text"
                                                                    placeholder="Enter PDF name"
                                                                    onChange={(e) =>
                                                                        setFileInputs((prev) => ({
                                                                            ...prev,
                                                                            [sub.id]: { ...prev[sub.id], name: e.target.value },
                                                                        }))
                                                                    }
                                                                    className="col-span-2 border rounded px-2 py-1"
                                                                />

                                                                <label className="font-medium">Select PDF File:</label>
                                                                <input
                                                                    type="file"
                                                                    accept="application/pdf"
                                                                    onChange={(e) => handleFileChange(e, sub.id, 'pdf')}
                                                                    className="col-span-2"
                                                                />

                                                                <span></span>
                                                                <button
                                                                    onClick={() => handlePdfUpload(sub.id)}
                                                                    className="col-span-2 bg-purple-600 text-white px-3 py-1 rounded hover:bg-purple-700"
                                                                >
                                                                    Upload PDF
                                                                </button>
                                                            </div>
                                                        </div>


                                                        {/* MCQ Upload */}
                                                        <div>
                                                            <McqUploadForm subTopicId={sub.id} />
                                                        </div>

                                                        {/* Videos Table */}
                                                        {sub.videos?.length > 0 && (
                                                            <div>
                                                                <h4 className="text-md font-semibold mb-2">Videos</h4>
                                                                <table className="w-full text-sm border">
                                                                    <thead>
                                                                        <tr className="bg-gray-200 dark:bg-gray-700 text-left">
                                                                            <th className="p-2">Thumbnail</th>
                                                                            <th className="p-2">Views</th>
                                                                            <th className="p-2">Actions</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        {sub.videos.map((video) => (
                                                                            <tr key={video.id} className="border-t">
                                                                                <td className="p-2">
                                                                                    <img
                                                                                        src={video.thumbnailUrl}
                                                                                        alt="thumbnail"
                                                                                        className="h-20 rounded"
                                                                                    />
                                                                                </td>
                                                                                <td className="p-2">{video.noOfViews}</td>
                                                                                <td className="p-2">
                                                                                    <button
                                                                                        onClick={() => setModalVideoUrl(video.url)}
                                                                                        className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
                                                                                    >
                                                                                        Play
                                                                                    </button>
                                                                                </td>
                                                                            </tr>
                                                                        ))}
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        )}

                                                        {/* Study Materials Table */}
                                                        {sub.studyMaterials?.length > 0 && (
                                                            <div>
                                                                <h4 className="text-md font-semibold mb-2">Study Materials</h4>
                                                                <table className="w-full text-sm border">
                                                                    <thead>
                                                                        <tr className="bg-gray-200 dark:bg-gray-700 text-left">
                                                                            <th className="p-2">Name</th>
                                                                            <th className="p-2">Version</th>
                                                                            <th className="p-2">Latest</th>
                                                                            <th className="p-2">Actions</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        {sub.studyMaterials.map((mat) => (
                                                                            <tr key={mat.id} className="border-t">
                                                                                <td className="p-2">{mat.name}</td>
                                                                                <td className="p-2">{mat.version}</td>
                                                                                <td className="p-2">{mat.isLatest ? 'Yes' : 'No'}</td>
                                                                                <td className="p-2">
                                                                                    <a
                                                                                        href={mat.pdfUrl}
                                                                                        target="_blank"
                                                                                        rel="noopener noreferrer"
                                                                                        className="text-indigo-600 underline"
                                                                                    >
                                                                                        Download
                                                                                    </a>
                                                                                </td>
                                                                            </tr>
                                                                        ))}
                                                                    </tbody>
                                                                </table>
                                                                <button
                                                                    onClick={() => handleDownloadAll(sub.id)}
                                                                    className="mt-2 px-3 py-1 text-sm bg-indigo-600 text-white rounded hover:bg-indigo-700"
                                                                >
                                                                    Download All PDFs
                                                                </button>
                                                            </div>
                                                        )}
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                ))
            )}

            {/* Video Modal */}
            <VideoPlayerModal
                isOpen={!!modalVideoUrl}
                videoUrl={modalVideoUrl}
                onClose={() => setModalVideoUrl(null)}
            />
        </div>


    );
};

export default ViewTopicsPage;
