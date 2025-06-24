import { useState } from 'react';
import userService from '../../services/userService';

const ResendVerification = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const msg = await userService.resendVerificationEmail(email);
      setMessage(msg);
    } catch {
      setMessage('Failed to resend verification email');
    }
  };

  return (
    <div className="max-w-sm mx-auto mt-10 p-4 bg-white shadow rounded">
      <h2 className="text-xl font-bold mb-4 text-center">Resend Verification Email</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="Enter your email"
          className="w-full p-2 border rounded mb-4"
          required
        />
        <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded">
          Resend Email
        </button>
      </form>
      {message && <p className="mt-4 text-center text-sm text-gray-600">{message}</p>}
    </div>
  );
};

export default ResendVerification;
