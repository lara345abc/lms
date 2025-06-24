// import { GoogleLogin, GoogleOAuthProvider } from '@react-oauth/google';
// import axios from 'axios';

// const clientId = '25562083860-ntumf7l83oh8n742qdehtonv99oks8bf.apps.googleusercontent.com';  //lms2

// const GoogleLoginButton = ({ setUserData }) => {
//     const handleSuccess = async (response) => {
//         const idToken = response.credential;
//         try {
//             const res = await axios.post('http://localhost:8080/api/oauth2/login', { idToken });
//             setUserData(res.data.data.user);
//             console.log(res.data.data.user);
//         } catch (err) {
//             console.error('Token verification failed', err);
//         }
//     };

//     return (
//         <GoogleOAuthProvider clientId={clientId}>
//            <GoogleLogin onSuccess={handleSuccess} onError={() => console.error('Google login failed')} />
//         </GoogleOAuthProvider>
//     );
// };

// export default GoogleLoginButton;


import { GoogleLogin, GoogleOAuthProvider } from '@react-oauth/google';
import authService from '../../services/authService';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

const clientId = '25562083860-ntumf7l83oh8n742qdehtonv99oks8bf.apps.googleusercontent.com';

const GoogleLoginButton = ({ setUserData }) => {
    const navigate = useNavigate();
    const handleSuccess = async (response) => {
        const idToken = response.credential;
        console.log("token +++", idToken)
        try {
            const response = await authService.loginWithGoogle(idToken);
            const { token, user } = response.data;

            const role = user.role || 'USER';

			localStorage.setItem('token', token);
			localStorage.setItem('role', role);
			localStorage.setItem('user', JSON.stringify(user));
			console.log(user)

			toast.success("Login Success");

			setTimeout(() => {
				if (role === 'admin') {
					navigate('/dashboard');
				} else {
					navigate('/user/my-dashboard');
				}
			}, 2000);
        } catch (err) {
            console.error('Google token verification failed', err);
        }
    };

    return (
        <GoogleOAuthProvider clientId={clientId}>
            <GoogleLogin
                onSuccess={handleSuccess}
                onError={() => console.error('Google login failed')}
            />
        </GoogleOAuthProvider>
    );
};

export default GoogleLoginButton;
