import React from "react";
import PackageList from "../../components/packages/PackageList";
import PackageForm from "../../components/packages/PackageForm";
import PackageDetails from "../../components/packages/PackageDetails";
import { Route, Routes } from "react-router-dom";

const PackagePage = () => {
  return (
    <div>
    <Routes>
      <Route path="/" element={<PackageList />} />
      <Route path="/create" element={<PackageForm />} />
      <Route path="/edit/:id" element={<PackageForm />} />
      <Route path=":id" element={<PackageDetails />} />
    </Routes>
    </div>
  );
};

export default PackagePage;