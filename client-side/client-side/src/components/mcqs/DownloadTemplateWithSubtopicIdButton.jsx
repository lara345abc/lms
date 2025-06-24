import React from "react";
import mcqService from "../../services/mcqService";

const DownloadTemplateWithSubtopicIdButton = () => {
  const handleDownload = async () => {
    try {
      const response = await mcqService.downloadTemplateWithSubTopic();
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "mcq-template-with-subtopic.xlsx");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("Error downloading subtopic template:", error);
      alert("Failed to download subtopic template.");
    }
  };

  return (
    <button
      onClick={handleDownload}
      className="bg-purple-600 hover:bg-purple-700 text-white px-3 py-1 text-sm rounded shadow"
    >
      Download MCQ Template (with SubTopic ID)
    </button>
  );
};

export default DownloadTemplateWithSubtopicIdButton;
