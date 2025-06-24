import axiosInstance from "../api/axiosInstance";

const commentService = {
    createComment: async ({ videoId, comment, rating }) => {
        const response = await axiosInstance.post("/comments", {
            videoId,
            comment,
            rating,
        });
        return response.data;
    },

    getCommentSummary: async (videoId) => {
        const response = await axiosInstance.get(`/comments/summary/${Number(videoId)}`);
        return response.data;
    },


    getCommentsByVideo: async (videoId) => {
        const response = await axiosInstance.get(`/comments/video/${videoId}`);
        return response.data;
    },

    getRatingDistribution: async (videoId) => {
        const response = await axiosInstance.get(`/comments/ratings/${videoId}`);
        return response.data;
    },

    getPagedCommentsByVideo: async (videoId, page = 0, size = 5) => {
        const response = await axiosInstance.get(`/comments/video/${videoId}/paged`, {
            params: { page, size },
        });
        return response.data;
    },


};

export default commentService;
