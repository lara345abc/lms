import axiosInstance from "../api/axiosInstance";

export const createSkill = (data) => axiosInstance.post('/skills/addSkill', data);

export const getSkillById = (id) => axiosInstance.get(`/skills/${id}`);

export const getAllSkills = () => axiosInstance.get('/skills');

export const getPagedSkills = (page = 0, size = 20) =>
  axiosInstance.get(`/skills/page?page=${page}&size=${size}`);

export const updateSkill = (id, data) => axiosInstance.put(`/skills/${id}`, data);

export const deleteSkill = (id) => axiosInstance.delete(`/skills/${id}`);

export const getSkillWithTopics = (id) => axiosInstance.get(`/skills/skills/${id}/topics`);


