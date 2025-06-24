import axiosInstance from "../api/axiosInstance";

const topicService = {
  // ------------------ TOPIC ------------------

  // Create new topic
  createTopic: async (dto) => {
    const response = await axiosInstance.post("/topic/addTopic", dto);
    return response.data;
  },

  // Get topic by ID
  getTopicById: async (id) => {
    const response = await axiosInstance.get(`/topic/${id}`);
    return response.data;
  },

  // Get all topics
  getAllTopics: async () => {
    const response = await axiosInstance.get("/topic");
    return response.data;
  },

  // Get paginated topics
  getTopicsPage: async (page = 0, size = 20) => {
    const response = await axiosInstance.get("/topic/page", {
      params: { page, size },
    });
    return response.data;
  },

  // Get topics by skill ID
  getTopicsBySkillId: async (skillId) => {
    const response = await axiosInstance.get(`/topic/by-skill/${skillId}`);
    return response.data;
  },

  // Get filtered topics
  getFilteredTopics: async (title = "", page = 0, size = 20) => {
    const response = await axiosInstance.get("/topic/filtered", {
      params: { title, page, size },
    });
    return response.data;
  },

  // Get topic with SubTopics + Videos + StudyMaterials
  getTopicWithDetails: async (topicId) => {
    const response = await axiosInstance.get("/topic/getTopicWithDetails", {
      params: { topicId },
    });
    return response.data;
  },

  // Update topic
  updateTopic: async (id, dto) => {
    const response = await axiosInstance.put(`/topic/${id}`, dto);
    return response.data;
  },

  // Delete topic
  deleteTopic: async (id) => {
    const response = await axiosInstance.delete(`/topic/${id}`);
    return response.data;
  },

  // ------------------ SUBTOPIC ------------------

  // Create new subtopic
  createSubTopic: async (dto) => {
    const response = await axiosInstance.post("/subTopic/addTopic", dto);
    return response.data;
  },

  // Get subtopic by ID
  getSubTopicById: async (id) => {
    const response = await axiosInstance.get(`/subTopic/${id}`);
    return response.data;
  },

  //Get subtoic by ID's
   getByIds: async (subTopicIds) => {
    const response = await axiosInstance.get("/subTopic/getByIds", {
      params: { subTopicIds },
      paramsSerializer: (params) =>
        params.subTopicIds.map((id) => `subTopicIds=${id}`).join("&"),
    });
    return response.data;
  },

  // Get all subtopics
  getAllSubTopics: async () => {
    const response = await axiosInstance.get("/subTopic");
    return response.data;
  },

  // Get paginated subtopics
  getSubTopicsPage: async (page = 0, size = 20) => {
    const response = await axiosInstance.get("/subTopic/page", {
      params: { page, size },
    });
    return response.data;
  },

  // Get filtered subtopics
  getFilteredSubTopics: async (title = "", page = 0, size = 20) => {
    const response = await axiosInstance.get("/subTopic/filtered", {
      params: { title, page, size },
    });
    return response.data;
  },

  // Update subtopic
  updateSubTopic: async (id, dto) => {
    const response = await axiosInstance.put(`/subTopic/${id}`, dto);
    return response.data;
  },

  // Delete subtopic
  deleteSubTopic: async (id) => {
    const response = await axiosInstance.delete(`/subTopic/${id}`);
    return response.data;
  },
};

export default topicService;
