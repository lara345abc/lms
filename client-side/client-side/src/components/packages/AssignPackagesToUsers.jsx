import React, { useState } from 'react';
import { assignPackagesToUsers } from '../../services/userService';
import toast from 'react-hot-toast';

const AssignPackagesToUsers = () => {
  const [userIds, setUserIds] = useState('');
  const [packageIds, setPackageIds] = useState('');

  const handleSubmit = async () => {
    try {
      const userIdArray = userIds.split(',').map(id => parseInt(id.trim()));
      const packageIdArray = packageIds.split(',').map(id => parseInt(id.trim()));
      await assignPackagesToUsers(userIdArray, packageIdArray);
      toast.success('Packages assigned to users');
    } catch (error) {
      toast.error('Error assigning packages');
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto bg-white rounded shadow">
      <h2 className="text-xl font-semibold mb-4">Assign Packages to Multiple Users</h2>
      <input
        type="text"
        placeholder="User IDs (comma-separated)"
        value={userIds}
        onChange={(e) => setUserIds(e.target.value)}
        className="input input-bordered w-full mb-2"
      />
      <input
        type="text"
        placeholder="Package IDs (comma-separated)"
        value={packageIds}
        onChange={(e) => setPackageIds(e.target.value)}
        className="input input-bordered w-full mb-2"
      />
      <button onClick={handleSubmit} className="btn btn-primary w-full">Assign</button>
    </div>
  );
};

export default AssignPackagesToUsers;
