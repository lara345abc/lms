import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createPackage, getPackageById, updatePackage } from "../../services/packageService";

const PackageForm = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('');
  const [skills, setSkills] = useState([
    { title: '', description: '', price: '' }
  ]);

  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    if (id) {
      getPackageById(id).then(({ data }) => {
        const pkg = data.data;
        setTitle(pkg.title);
        setDescription(pkg.description);
        setPrice(pkg.price);
        setSkills(pkg.skills || [{ title: '', description: '', price: '' }]);
      });
    }
  }, [id]);

  const handleSkillChange = (index, field, value) => {
    const updatedSkills = [...skills];
    updatedSkills[index][field] = value;
    setSkills(updatedSkills);
  };

  const addSkill = () => {
    setSkills([...skills, { title: '', description: '', price: '' }]);
  };

  const removeSkill = (index) => {
    const updatedSkills = [...skills];
    updatedSkills.splice(index, 1);
    setSkills(updatedSkills);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const packageData = {
      title,
      description,
      price: parseFloat(price),
      skills: skills.map(skill => ({
        ...skill,
        price: parseFloat(skill.price)
      }))
    };
    try {
      if (id) {
        await updatePackage(id, packageData);
      } else {
        await createPackage(packageData);
      }
      navigate('/package');
    } catch (error) {
      console.error('Error submitting package form', error);
    }
  };

  return (
    <div className="p-6 max-w-full  bg-white dark:bg-gray-900 rounded shadow">
      <h2 className="text-2xl font-bold mb-4 text-gray-800 dark:text-white">
        {id ? 'Edit Package' : 'Create Package'}
      </h2>
      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Title */}
        <div>
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Title</label>
          <input
            type="text"
            className="w-full p-2 border rounded dark:bg-gray-800 dark:text-white"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>

        {/* Description */}
        <div>
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Description</label>
          <textarea
            className="w-full p-2 border rounded dark:bg-gray-800 dark:text-white"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>

        {/* Price */}
        <div>
          <label className="block text-sm font-medium text-gray-700 dark:text-gray-300">Price</label>
          <input
            type="number"
            step="0.01"
            className="w-full p-2 border rounded dark:bg-gray-800 dark:text-white"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
        </div>

        {/* Skills */}
        <div>
          <label className="block text-lg font-semibold text-gray-800 dark:text-white mb-2">Skills</label>
          {skills.map((skill, index) => (
            <div key={index} className="space-y-2 mb-4 border p-4 rounded bg-gray-50 dark:bg-gray-800">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <input
                  type="text"
                  placeholder="Skill Title"
                  className="p-2 border rounded dark:bg-gray-700 dark:text-white"
                  value={skill.title}
                  onChange={(e) => handleSkillChange(index, 'title', e.target.value)}
                  required
                />
                <input
                  type="text"
                  placeholder="Description"
                  className="p-2 border rounded dark:bg-gray-700 dark:text-white"
                  value={skill.description}
                  onChange={(e) => handleSkillChange(index, 'description', e.target.value)}
                  required
                />
                <input
                  type="number"
                  step="0.01"
                  placeholder="Price"
                  className="p-2 border rounded dark:bg-gray-700 dark:text-white"
                  value={skill.price}
                  onChange={(e) => handleSkillChange(index, 'price', e.target.value)}
                  required
                />
              </div>
              {skills.length > 1 && (
                <button
                  type="button"
                  onClick={() => removeSkill(index)}
                  className="text-red-500 text-sm mt-2"
                >
                  Remove Skill
                </button>
              )}
            </div>
          ))}
          <button
            type="button"
            onClick={addSkill}
            className="text-blue-500 hover:underline text-sm"
          >
            + Add Another Skill
          </button>
        </div>

        {/* Submit */}
        <button
          type="submit"
          className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
        >
          {id ? 'Update Package' : 'Create Package'}
        </button>
      </form>
    </div>
  );
};

export default PackageForm;
