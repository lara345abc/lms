import { useEffect, useState } from 'react';
import userService from '../../services/userService';
import { FaSpinner } from 'react-icons/fa';

const UserDetails = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await userService.getUserByEmail();
        setUser(response.data);
        console.log(response.data)

      } catch (error) {
        console.error('Failed to fetch user:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  if (loading) {
    return (
      <div className="text-center p-10">
        <FaSpinner className="animate-spin text-5xl text-indigo-600 mx-auto" />
        <p className="mt-4 text-gray-500">Loading user details...</p>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="text-center p-10 text-red-500">
        <p>Failed to load user information.</p>
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto mt-10 p-6 shadow-lg rounded-xl bg-white">
      <img
        src={user.pictureUrl}
        alt={user.name}
        className="w-24 h-24 rounded-full mx-auto mb-4 ring-2 ring-indigo-400"
        referrerPolicy="no-referrer"
      />
      <h2 className="text-2xl font-semibold text-center text-indigo-700">{user.name}</h2>
      <p className="text-center text-gray-700">{user.email}</p>
      {/* <p className="text-center text-sm text-gray-500 mt-2">Role: {user.role}</p> */}
    </div>
  );
};

export default UserDetails;
