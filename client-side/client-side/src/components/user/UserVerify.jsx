import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import userService from '../../services/userService';

const UserVerify = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const [message, setMessage] = useState('Verifying...');

  useEffect(() => {
    if (token) {
      userService.verifyUser(token)
        .then(msg => setMessage(msg))
        .catch(() => setMessage('Verification failed'));
    }
  }, [token]);

  return <div className="text-center mt-10 text-lg font-medium">{message}</div>;
};

export default UserVerify;