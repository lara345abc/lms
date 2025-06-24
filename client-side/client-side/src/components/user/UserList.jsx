import { useEffect, useState } from 'react';
import userService from '../../services/userService';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);

  useEffect(() => {
    userService.getPaginatedUsers(page).then(data => setUsers(data.content)).catch(console.error);
  }, [page]);

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold text-center mb-4">All Users</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {users.map(user => (
          <div key={user.id} className="bg-white shadow p-4 rounded-lg">
            <img src={user.pictureUrl} alt={user.name} className="w-16 h-16 rounded-full mb-2" />
            <h3 className="font-semibold">{user.name}</h3>
            <p>{user.email}</p>
            <p className="text-sm text-gray-500">Role: {user.role}</p>
          </div>
        ))}
      </div>
      <div className="flex justify-center mt-4">
        <button
          onClick={() => setPage(prev => Math.max(prev - 1, 0))}
          className="px-4 py-2 bg-blue-500 text-white rounded mx-2"
        >
          Prev
        </button>
        <button
          onClick={() => setPage(prev => prev + 1)}
          className="px-4 py-2 bg-blue-500 text-white rounded mx-2"
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default UserList;