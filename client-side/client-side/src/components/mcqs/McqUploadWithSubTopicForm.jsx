import React, { useRef, useState } from "react";
import mcqService from "../../services/mcqService";
import toast from "react-hot-toast";
import DownloadTemplateWithSubtopicIdButton from "./DownloadTemplateWithSubtopicIdButton";

const McqUploadWithSubTopicForm = () => {
  const [file, setFile] = useState(null);
  const [summary, setSummary] = useState(null);
  const inputRef = useRef();

  const handleUpload = async () => {
    if (!file) {
      toast.error("Please select a file");
      return;
    }

    try {
      const result = await mcqService.uploadMcqsWithSubTopicExcel(file);
      setSummary(result.data);
      toast.success("MCQs uploaded successfully");
      setFile(null);
      inputRef.current.value = null;
    } catch (err) {

      toast.error("Upload failed");
      console.error(err);
    }
  };

  return (
    <div className="p-4 border rounded shadow bg-white space-y-4">
      <h4 className="text-md font-semibold">🧩 Upload MCQs At once With SubTopic ID</h4>
      <input
        type="file"
        accept=".xlsx"
        ref={inputRef}
        onChange={(e) => setFile(e.target.files[0])}
        className="text-sm"
      />
      <button
        onClick={handleUpload}
        className="bg-purple-600 hover:bg-purple-700 text-white px-4 py-1 mx-2 text-sm rounded"
      >
        Upload File
      </button>
      <DownloadTemplateWithSubtopicIdButton />

      {summary && (
        <div className="text-sm text-gray-800">
          <p className="text-green-700 font-medium">
            ✅ Uploaded: {summary.uploaded} | ❌ Skipped: {summary.skipped}
          </p>
          <ul className="list-disc ml-5 mt-2 text-red-600">
            {summary.skippedDetails.map((msg, idx) => (
              <li key={idx}>{msg}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default McqUploadWithSubTopicForm;
