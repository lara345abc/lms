import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";
import Navbar from './components/navbar/Navbar';
import AuthForm from './components/auth/AuthForm';
import UserDetailPage from './pages/UserDetailsPage';
import UserListPage from './pages/UserListPage';
// import VerifyUserPage from './pages/VerifyUserPage';
import ResendVerificationPage from './pages/ResendVerificationPage';
import AdminDashboard from './pages/adminPages/AdminDashboard';
import PackageList from './components/packages/PackageList';
import PackageForm from './components/packages/PackageForm';
import PackageDetails from './components/packages/PackageDetails';
import { Toaster } from 'react-hot-toast';
import Sidebar from './components/navbar/Sidebar';
import AddTopicsPage from './components/packages/AddTopicsPage';
import ViewTopicsPage from './components/packages/ViewTopicsPage';
import SubTopicDetails from './components/packages/SubTopicsDetails';
import UserDashboard from './components/user/UserDashboard';
import VideoDispaly from './components/packages/VideoDisplay';
import PaginatedUserList from './components/PaginatedUserList';
import AssignPackagesToUser from './components/packages/AssignPackagesToUser';
import UserPackageAssigner from './components/packages/UserPackageAssigner';
import UserDetails from './components/user/UserDetails';
import MyPackages from './components/packages/MyPackages';
import SubTopicMcqs from './components/mcqs/SubTopicMcqs';
import McqTest from './components/mcqs/McqTest';
import SubTopicAttemptHistory from './components/mcqs/SubTopicAttemptHistory';
import McqTestResults from './components/skills/SkillTopicsTable';

function AppWrapper() {
  const location = useLocation();
  const hideSidebarRoutes = ['/'];

  const shouldHideSidebar = hideSidebarRoutes.includes(location.pathname);

  return (
    <>
      <Toaster />
      {shouldHideSidebar ? (
        <Routes>
          <Route path="/" element={<AuthForm />} />
        </Routes>
      ) : (
        <Sidebar>
          <Routes>
            <Route path="/dashboard" element={<AdminDashboard />} />
            <Route path="/user/detail" element={<UserDetailPage />} />
            <Route path="/user/list" element={<UserListPage />} />
            {/* <Route path="/user/verify" element={<VerifyUserPage />} /> */}
            <Route path="/user/settings" element={<UserDetails />} />
            <Route path="/user/packages" element={<MyPackages />} />
            <Route path="/user/resend-verification" element={<ResendVerificationPage />} />
            <Route path="/package" element={<PackageList />} />
            <Route path="/package/create" element={<PackageForm />} />
            <Route path="/package/edit/:id" element={<PackageForm />} />
            <Route path="/package/:id" element={<PackageDetails />} />
            <Route path="/topic/add/:skillId" element={<AddTopicsPage />} />
            <Route path="/skill/:skillId/topics" element={<ViewTopicsPage />} />
            <Route path="/subtopic/:id" element={<SubTopicDetails />} />
            <Route path="/user/my-dashboard" element={<UserDashboard />} />
            <Route path="/topics/:id" element={<VideoDispaly />} />
            <Route path="/admin/paginated-users" element={<PaginatedUserList />} />
            <Route path="/admin/package-to-user" element={<AssignPackagesToUser />} />
            <Route path="/admin/package-assigner" element={<UserPackageAssigner />} />
            <Route path="/subtopic-mcqs/:subTopicIds" element={<SubTopicMcqs />} />
            <Route path="/mcq-test/:subTopicIds" element={<McqTest />} />
            <Route path="/mcq-test/attempt-history/:subTopicId" element={<SubTopicAttemptHistory />} />
            <Route path="/mcq-test/resutls/:skillId" element={<McqTestResults />} />
            {/* <Route path="/admin/package-assigner" element={<PackageList />} /> */}
            {/* <Route path="/skill/create" element={<SkillForm />} />
            <Route path="/topic/create" element={<TopicForm />} />
            <Route path="/materials" element={<MaterialList />} />
            <Route path="/videos" element={<VideoList />} />
            <Route path="/mcqs" element={<McqList />} />
            <Route path="/approvals" element={<PendingApprovalPage />} /> */}
          </Routes>
        </Sidebar>
      )}
    </>
  );
}
function App() {
  return (
    <Router>
      <AppWrapper />
    </Router>
  );
}

export default App;
