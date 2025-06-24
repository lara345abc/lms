import React, { useEffect, useState } from 'react';
import { getPackageById } from '../../services/packageService';
import { useParams } from 'react-router-dom';

const PackageDetails = () => {
  const { id } = useParams();
  const [pkg, setPkg] = useState(null);

  useEffect(() => {
    getPackageById(id).then(({ data }) => {
      setPkg(data.data);
    });
  }, [id]);

  if (!pkg) return <div className="p-6">Loading...</div>;

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Package Details</h2>
      <p><strong>ID:</strong> {pkg.id}</p>
      <p><strong>Title:</strong> {pkg.title}</p>
      <p><strong>Price:</strong> ${pkg.price}</p>
    </div>
  );
};

export default PackageDetails;