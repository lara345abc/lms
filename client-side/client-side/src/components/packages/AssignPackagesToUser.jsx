import React, { useState } from 'react';
import toast from 'react-hot-toast';
import { assignPackagesToUser } from '../../services/packageService';

const AssignPackagesToUser = () => {
  const [userId, setUserId] = useState('');
  const [packageIds, setPackageIds] = useState('');

  const handleSubmit = async () => {
    try {
      const ids = packageIds.split(',').map(id => parseInt(id.trim()));
      await assignPackagesToUser(userId, ids);
      toast.success('Packages assigned to user');
    } catch (error) {
      toast.error('Error assigning packages');
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto bg-white rounded shadow">
      <h2 className="text-xl font-semibold mb-4">Assign Packages to User</h2>
      <input
        type="text"
        placeholder="User ID"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
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

export default AssignPackagesToUser;
