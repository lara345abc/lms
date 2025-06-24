// import React, { useState } from "react";
// import mcqService from "../../services/mcqService";
// import toast from "react-hot-toast";

// const McqUploadForm = ({ subTopicId }) => {
//     const [file, setFile] = useState(null);
//     const [responseSummary, setResponseSummary] = useState(null);

//     const handleSubmit = async (e) => {
//         e.preventDefault();

//         if (!file || !subTopicId) {
//             toast.error("File or SubTopic ID missing");
//             return;
//         }

//         try {
//             const result = await mcqService.uploadMcqsExcel(file, subTopicId);
//             setResponseSummary(result.data);
//             toast.success("Upload completed");
//         } catch (error) {
//             console.error("Upload error:", error);
//             toast.error("Upload failed");
//         }
//     };

//     return (
//         <div className="p-4 max-w-xl mx-auto bg-white rounded shadow">
//             <h2 className="text-xl font-bold mb-4">Upload MCQ Excel</h2>

//             <form onSubmit={handleSubmit} className="space-y-4">
//                 <input
//                     type="file"
//                     accept=".xlsx"
//                     onChange={(e) => setFile(e.target.files[0])}
//                     className="w-full"
//                     required
//                 />

//                 <button
//                     type="submit"
//                     className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
//                 >
//                     Upload Excel
//                 </button>
//             </form>

//             {responseSummary && (
//                 <div className="mt-6 p-4 border rounded bg-gray-50">
//                     <h3 className="font-semibold mb-2 text-gray-800">Upload Summary:</h3>
//                     <p><strong>Uploaded:</strong> {responseSummary.uploaded}</p>
//                     <p><strong>Skipped:</strong> {responseSummary.skipped}</p>

//                     {responseSummary.skippedDetails?.length > 0 && (
//                         <div className="mt-2">
//                             <p className="font-medium text-red-600">Skipped Details:</p>
//                             <ul className="list-disc ml-6 text-sm text-gray-700">
//                                 {responseSummary.skippedDetails.map((msg, idx) => (
//                                     <li key={idx}>{msg}</li>
//                                 ))}
//                             </ul>
//                         </div>
//                     )}
//                 </div>
//             )}
//         </div>
//     );
// };

// export default McqUploadForm;


import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import mcqService from "../../services/mcqService";
import toast from "react-hot-toast";
import DownloadTemplateButton from "./DownloadTemplateButton";

const McqUploadForm = ({ subTopicId }) => {
    const [file, setFile] = useState(null);
    const [summary, setSummary] = useState(null);
    const inputRef = useRef();
    const navigate = useNavigate();

    const handleUpload = async () => {
        if (!file) {
            toast.error("Please select an Excel file");
            return;
        }

        try {
            const result = await mcqService.uploadMcqsExcel(file, subTopicId);
            setSummary(result.data);
            toast.success("MCQs uploaded successfully");
            setFile(null);
            inputRef.current.value = null;
        } catch (err) {
            toast.error("Upload failed");
            console.error(err);
        }
    };

    const handleViewQuestions = () => {
        navigate(`/subtopic-mcqs/${subTopicId}`);
    };

    return (
        <div>
            <h4 className="text-md font-semibold mb-2">📝 MCQ Upload</h4>
            <div className="grid grid-cols-3 gap-4 items-center text-sm mb-2">
                <label className="font-medium">Select Excel File:</label>
                <input
                    type="file"
                    accept=".xlsx"
                    ref={inputRef}
                    onChange={(e) => setFile(e.target.files[0])}
                    className="col-span-2"
                />

                <span></span>
                <div className="col-span-2 flex gap-2">
                    <button
                        onClick={handleUpload}
                        className="bg-blue-600 text-white px-3 py-1 rounded text-xs hover:bg-blue-700"
                    >
                        Upload MCQs to this Sub topic
                    </button>
                    <button
                        onClick={handleViewQuestions}
                        className="bg-gray-600 text-white px-3 py-1 rounded text-xs hover:bg-gray-700"
                    >
                        View Existing Questions
                    </button>
                    <DownloadTemplateButton />
                </div>

                {summary && (
                    <>
                        <span className="font-medium text-green-700 col-span-3">
                            ✅ Uploaded: {summary.uploaded} | ❌ Skipped: {summary.skipped}
                        </span>
                        <ul className="list-disc ml-6 text-sm text-gray-700 col-span-3">
                            {summary.skippedDetails.map((msg, idx) => (
                                <li key={idx}>{msg}</li>
                            ))}
                        </ul>
                    </>
                )}
            </div>
        </div>
    );
};

export default McqUploadForm;
