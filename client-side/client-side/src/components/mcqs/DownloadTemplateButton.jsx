import React from "react";
import mcqService from "../../services/mcqService";

const DownloadTemplateButton = () => {
  const handleDownload = async () => {
    try {
      const response = await mcqService.downloadTemplate();
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "mcq-template.xlsx");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("Error downloading template:", error);
      alert("Failed to download template.");
    }
  };

  return (
    <button
      onClick={handleDownload}
      className="bg-blue-600 hover:bg-blue-700 text-white px-2 py-1 text-xs rounded shadow"
    >
      Download MCQ Template
    </button>
  );
};

export default DownloadTemplateButton;
