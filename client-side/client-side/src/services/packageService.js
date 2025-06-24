import axiosInstance from "../api/axiosInstance";

export const createPackage = (data) => axiosInstance.post('/packages/createPackage', data);

export const getPackageById = (id) => axiosInstance.get(`/packages/${id}`);

export const getAllPackages = () => axiosInstance.get('/packages/getAllPackage');

// export const getAllPackages = () => axiosInstance.get('/packages/getAllPackageWithSkills');

export const getPagedPackages = (page = 0, size = 20) => axiosInstance.get(`/packages/paged?page=${page}&size=${size}`);

export const findAllWithSkills = (page = 0, size = 20) => axiosInstance.get(`/packages/findAllWithSkills?page=${page}&size=${size}`);

export const updatePackage = (id, data) => axiosInstance.put(`/packages/${id}`, data);

export const deletePackage = (id) => axiosInstance.delete(`/packages/${id}`);


export const createTopic = (data) => axiosInstance.post('/topic/addTopic', data);

export const getTopicById = (id) => axiosInstance.get(`/topic/${id}`);

export const getAllTopics = () => axiosInstance.get('/topic/getAll');

export const updateTopic = (id, data) => axiosInstance.put(`/topic/${id}`, data);

export const deleteTopic = (id) => axiosInstance.delete(`/topic/${id}`);

export const getTopicsBySkillId = (skillId) => axiosInstance.get(`/topic/by-skill/${skillId}`);

export const getSubTopicById = (subTopicId) => axiosInstance.get(`/subTopic/${subTopicId}`);

export const uploadVideoAndThumbnail = (subTopicId, formData, onUploadProgress) => {
 console.log("subtopic id in service ", subTopicId)
  axiosInstance.post(`/videos/upload/${subTopicId}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    onUploadProgress,
  });
}

export const uploadStudyMaterial = (subTopicId, formData) =>
  axiosInstance.post(`/study-materials/upload`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });

  export const downloadAllStudyMaterials = (subTopicId) =>
  axiosInstance.get(`/study-materials/download-all/${subTopicId}`, {
    responseType: 'blob', 
  });


export const getFilteredPackages = (filters) => {
  const { title, minPrice, maxPrice, page = 0, size = 20 } = filters;
  const params = new URLSearchParams();
  if (title) params.append('title', title);
  if (minPrice) params.append('minPrice', minPrice);
  if (maxPrice) params.append('maxPrice', maxPrice);
  params.append('page', page);
  params.append('size', size);

  return axiosInstance.get(`/packages/filtered?${params.toString()}`);
};
export const assignPackagesToUser = (userId, packageIds) =>
  axiosInstance.put(`/user/${userId}/assign-packages`, { packageIds });

export const assignPackagesToUsers = (userIds, packageIds) =>
  axiosInstance.put(`/user/assign-packages-to-users`, { userIds, packageIds });

export const getAllUsers = () => axiosInstance.get('/user/all');