import axiosInstance from "../api/axiosInstance";

const mcqAttemptService = {
    // Save MCQ Attempt (automatically links user by Principal on backend)
    createAttempt: async (attemptDTO) => {
        const response = await axiosInstance.post("/mcq-attempts/create", attemptDTO);
        return response.data;
    },

    // Get attempt by ID
    getAttemptById: async (id) => {
        const response = await axiosInstance.get("/mcq-attempts/getById", {
            params: { id },
        });
        return response.data;
    },

    // Get attempts by userId
    getAttemptsByUserId: async (userId) => {
        const response = await axiosInstance.get("/mcq-attempts/byUser", {
            params: { userId },
        });
        return response.data;
    },

    // Get attempts by subTopicId
    getAttemptsBySubTopicId: async (subTopicId) => {
        const response = await axiosInstance.get("/mcq-attempts/bySubTopic", {
            params: { subTopicId },
        });
        return response.data;
    },

    // services/mcqAttemptService.js
    getAttemptsByMultipleSubTopicIds: async (subTopicIds) => {
        const response = await axiosInstance.get(`/mcq-attempts/byMultipleSubTopicIds`, {
            params: { ids: subTopicIds.join(',') },
        });
        return response.data;
    }

};

export default mcqAttemptService;
