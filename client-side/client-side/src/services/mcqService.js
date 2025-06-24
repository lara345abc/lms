import axiosInstance from "../api/axiosInstance";

const mcqService = {
  // Upload MCQs via Excel
  uploadMcqsExcel: async (file, subTopicId) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("subTopicId", subTopicId);

    const response = await axiosInstance.post("/mcqs/uploadExcel", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  },

  // Upload MCQs with subTopicId column in Excel
  uploadMcqsWithSubTopicExcel: async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    const response = await axiosInstance.post("/mcqs/uploadExcelWithSubTopic", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  },


  // Download Excel template
  downloadTemplate: async () => {
    const response = await axiosInstance.get("/mcqs/download-template", {
      responseType: "blob",
    });
    return response;
  },


  // Download template with subtopic
  downloadTemplateWithSubTopic: async () => {
    const response = await axiosInstance.get("/mcqs/download-template-with-subtopic", {
      responseType: "blob",
    });
    return response;
  },


  // Create a new MCQ
  createMcq: async (mcqDTO) => {
    const response = await axiosInstance.post("/mcqs/create", mcqDTO);
    return response.data;
  },

  // Update an existing MCQ by ID
  updateMcq: async (id, mcqDTO) => {
    const response = await axiosInstance.put("/mcqs/update", mcqDTO, {
      params: { id },
    });
    return response.data;
  },

  // Delete MCQ by ID
  deleteMcq: async (id) => {
    const response = await axiosInstance.delete("/mcqs/delete", {
      params: { id },
    });
    return response.data;
  },

  // Get MCQ by ID
  getMcqById: async (id) => {
    const response = await axiosInstance.get("/mcqs/getById", {
      params: { id },
    });
    return response.data;
  },

  // Get all MCQs
  getAllMcqs: async () => {
    const response = await axiosInstance.get("/mcqs/getAll");
    return response.data;
  },

  // Get all MCQs by SubTopic ID
  getBySubTopic: async (subTopicId) => {
    const response = await axiosInstance.get("/mcqs/getBySubTopic", {
      params: { subTopicId },
    });
    return response.data;
  },

  getBySubTopic: async (subTopicIds) => {
    const response = await axiosInstance.get("/mcqs/getBySubTopics", {
      params: { subTopicIds },
      paramsSerializer: (params) => params.subTopicIds.map(id => `subTopicIds=${id}`).join('&'),
    });
    return response.data;
  }

};

export default mcqService;
