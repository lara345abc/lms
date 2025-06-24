import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import { assignPackagesToUser, getAllPackages, getAllUsers } from '../../services/packageService';

const UserPackageAssigner = () => {
  const [users, setUsers] = useState([]);
  const [packages, setPackages] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [selectedPackageIds, setSelectedPackageIds] = useState([]);
  const [unassignedPackages, setUnassignedPackages] = useState([]);
  const [showAssignedModal, setShowAssignedModal] = useState(false);
  const [assignedPackages, setAssignedPackages] = useState([]);
  const [assignedUserName, setAssignedUserName] = useState('');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    getAllUsers()
      .then(res => {
        setUsers(res.data.data)
        console.log(res.data)

      }
      )
      .catch(err => {
        console.log(err)
        toast.error("Failed to fetch users")

      }
      );

    getAllPackages()
      .then(res => setPackages(res.data.data))
      .catch(err => toast.error("Failed to fetch packages"));
  }, []);

  const openAssignModal = (user) => {
    setSelectedUser(user);

    const assignedIds = user.packages?.map(pkg => pkg.id) || [];

    // Filter to show only unassigned packages
    const unassigned = packages.filter(pkg => !assignedIds.includes(pkg.id));

    // Store only unassigned package IDs for checkbox selection (optional if you want pre-checked)
    setSelectedPackageIds([]); // or keep pre-selected ones if needed

    // Store unassigned packages temporarily for modal rendering
    setUnassignedPackages(unassigned);
  };

  const openAssignedModal = (user) => {
    setAssignedPackages(user.packages || []);
    setAssignedUserName(user.name);
    setShowAssignedModal(true);
  };



  const handleCheckboxChange = (pkgId) => {
    setSelectedPackageIds(prev =>
      prev.includes(pkgId)
        ? prev.filter(id => id !== pkgId)
        : [...prev, pkgId]
    );
  };

  const handleAssign = async () => {
    try {
      await assignPackagesToUser(selectedUser.id, selectedPackageIds);
      toast.success("Packages assigned!");
      setSelectedUser(null);
    } catch (error) {
      toast.error("Failed to assign packages");
    }
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Assign Packages to Users</h2>
      <div className="mb-4">
        <input
          type="text"
          placeholder="Search by name or email..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="input input-bordered w-full max-w-sm"
        />
      </div>

      <table className="table table-zebra w-full">
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Name</th>
            <th>Assigned Packages</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {users
            .filter(user =>
              user.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
              user.email?.toLowerCase().includes(searchTerm.toLowerCase())
            )
            .map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.email}</td>
                <td>{user.name}</td>
                <td className="flex items-center space-x-2">
                  <span>{user.packages?.length || 0}</span>
                  {user.packages?.length > 0 && (
                    <button
                      onClick={() => openAssignedModal(user)}
                      className="btn btn-xs btn-outline btn-info"
                    >
                      View
                    </button>
                  )}
                </td>
                <td>
                  <button
                    onClick={() => openAssignModal(user)}
                    className="btn btn-sm btn-info"
                  >
                    Assign
                  </button>
                </td>
              </tr>
            ))}
        </tbody>
      </table>

      {/* Modal */}
      {selectedUser && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded shadow max-w-lg w-full">
            <h3 className="text-xl font-bold mb-4">Assign Packages to {selectedUser.name}</h3>

            <div className="max-h-64 overflow-y-auto space-y-2">
              {unassignedPackages.map(pkg => (
                <div key={pkg.id} className="flex items-center">
                  <input
                    type="checkbox"
                    checked={selectedPackageIds.includes(pkg.id)}
                    onChange={() => handleCheckboxChange(pkg.id)}
                    className="mr-2"
                  />
                  <span>{pkg.title}</span>
                </div>
              ))}

            </div>

            <div className="mt-4 flex justify-end space-x-2">
              <button onClick={() => setSelectedUser(null)} className="btn btn-secondary">
                Cancel
              </button>
              <button onClick={handleAssign} className="btn btn-primary">
                Assign
              </button>
            </div>
          </div>
        </div>
      )}

      {showAssignedModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded shadow max-w-lg w-full">
            <h3 className="text-xl font-bold mb-4">
              Assigned Packages for {assignedUserName}
            </h3>

            <div className="max-h-64 overflow-y-auto ">
              {assignedPackages.map(pkg => (
                <div key={pkg.id} className="border p-2 rounded bg-gray-100">
                  <h4 className="font-semibold">{pkg.title}</h4>
                  <p className="text-sm">{pkg.description}</p>
                </div>
              ))}
            </div>

            <div className="mt-4 flex justify-end">
              <button
                onClick={() => setShowAssignedModal(false)}
                className="btn btn-secondary"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};

export default UserPackageAssigner;
