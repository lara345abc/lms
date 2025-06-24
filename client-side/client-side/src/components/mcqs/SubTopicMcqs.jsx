import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import mcqService from "../../services/mcqService";
import toast from "react-hot-toast";
import SubTopicDetailsViewer from "../topic/SubTopicDetailsViewer";

const SubTopicMcqs = () => {
  const { subTopicIds } = useParams();
  const [mcqs, setMcqs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState({ question: "", options: {}, correctOption: "" });

  useEffect(() => {
    fetchMcqs();
  }, [subTopicIds]);

  const fetchMcqs = async () => {
    try {
      setLoading(true);
      const ids = subTopicIds.split(",").map(Number);
      const response = await mcqService.getBySubTopic(ids);
      setMcqs(response.data);
    } catch (err) {
      toast.error("Error fetching MCQs");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (mcq) => {
    setEditingId(mcq.id);
    setEditForm({
      question: mcq.question,
      options: JSON.parse(mcq.options),
      correctOption: mcq.correctOption,
    });
  };
  const handleEditChange = (field, value) => {
    setEditForm((prev) => {
      // If it's an existing option key, update the options map
      if (field in prev.options) {
        return {
          ...prev,
          options: {
            ...prev.options,
            [field]: value,
          },
        };
      }

      // If modifying correctOption (checkboxes), handle multi-select
      if (field === "correctOption") {
        const current = prev.correctOption?.split(",").map((s) => s.trim()) || [];
        const updated = current.includes(value)
          ? current.filter((v) => v !== value)
          : [...current, value];
        return { ...prev, correctOption: updated.join(",") };
      }

      // Fallback: other fields like question
      return { ...prev, [field]: value };
    });
  };


  const handleUpdate = async () => {
    try {
      const updatedMcq = {
        ...editForm,
        options: JSON.stringify(editForm.options),
      };
      await mcqService.updateMcq(editingId, updatedMcq);
      toast.success("MCQ updated");
      setEditingId(null);
      fetchMcqs();
    } catch (err) {
      toast.error("Update failed");
    }
  };

  const handleDelete = async (id) => {
    try {
      await mcqService.deleteMcq(id);
      toast.success("MCQ deleted");
      fetchMcqs();
    } catch (err) {
      toast.error("Delete failed");
    }
  };

  return (
    <div className="p-6 max-w-5xl mx-auto">
      <h2 className="text-xl font-semibold mb-4">MCQs for SubTopic(s)</h2>
      <SubTopicDetailsViewer subTopicIds={subTopicIds.split(",").map(Number)} />

      {loading ? (
        <p>Loading...</p>
      ) : mcqs.length === 0 ? (
        <p>No MCQs found.</p>
      ) : (
        <table className="w-full border text-sm">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-2 border">#</th>
              <th className="p-2 border">Question</th>
              <th className="p-2 border">Options</th>
              <th className="p-2 border">Correct</th>
              <th className="p-2 border">Actions</th>
            </tr>
          </thead>
          <tbody>
            {mcqs.map((mcq, index) =>
              editingId === mcq.id ? (
                <tr key={mcq.id} className="border-t bg-yellow-50">
                  <td className="p-2 border">{index + 1}</td>
                  <td className="p-2 border">
                    <input
                      value={editForm.question}
                      onChange={(e) => handleEditChange("question", e.target.value)}
                      className="w-full border px-2 py-1"
                    />
                  </td>
                  <td className="p-2 border space-y-1">
                    {["A", "B", "C", "D"].map((opt) => (
                      <div key={opt}>
                        <label>{opt}: </label>
                        <input
                          value={editForm.options[opt] || ""}
                          onChange={(e) => handleEditChange(opt, e.target.value)}
                          className="w-full border px-2 py-1 mt-1"
                        />
                      </div>
                    ))}
                  </td>
                  <td className="p-2 border">
                    <div className="space-y-1">
                      {Object.keys(editForm.options).map((opt) => (
                        <div key={opt} className="flex items-center space-x-1">
                          <input
                            type="checkbox"
                            checked={editForm.correctOption.split(",").includes(opt)}
                            onChange={() => handleEditChange("correctOption", opt)}
                          />
                          <label>{opt}</label>
                        </div>
                      ))}
                    </div>
                  </td>
                  <td className="p-2 border space-x-2">
                    <button
                      onClick={handleUpdate}
                      className="text-xs bg-green-600 text-white px-2 py-1 rounded hover:bg-green-700"
                    >
                      Save
                    </button>
                    <button
                      onClick={() => setEditingId(null)}
                      className="text-xs bg-gray-400 text-white px-2 py-1 rounded hover:bg-gray-500"
                    >
                      Cancel
                    </button>
                  </td>
                </tr>
              ) : (
                <tr key={mcq.id} className="border-t">
                  <td className="p-2 border">{index + 1}</td>
                  <td className="p-2 border">{mcq.question}</td>
                  <td className="p-2 border">
                    {Object.entries(JSON.parse(mcq.options)).map(([key, val]) => (
                      <div key={key}>
                        <strong>{key}:</strong> {val}
                      </div>
                    ))}
                  </td>
                  <td className="p-2 border">{mcq.correctOption}</td>
                  <td className="p-2 border space-x-2">
                    <button
                      onClick={() => handleEdit(mcq)}
                      className="text-xs bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(mcq.id)}
                      className="text-xs bg-red-600 text-white px-2 py-1 rounded hover:bg-red-700"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              )
            )}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default SubTopicMcqs;
