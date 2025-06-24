import React, { useState } from "react";
import commentService from "../../services/commentService";
import toast from "react-hot-toast";

const CommentModal = ({ isOpen, onClose, videoId, onCommentSubmit }) => {
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");

  if (!isOpen) return null;

  const handleSubmit = async () => {
    if (!rating || !comment.trim()) {
      toast.error("Please provide both rating and comment");
      return;
    }

    try {
      await commentService.createComment({ videoId, comment, rating });
      toast.success("Comment submitted");
      setRating(0);
      setComment("");
      onClose();
      if (onCommentSubmit) onCommentSubmit();
    } catch (error) {
      toast.error("Failed to submit comment");
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-lg w-full max-w-md p-6 relative">
        <button
          onClick={onClose}
          className="absolute top-2 right-3 text-gray-500 hover:text-black text-xl"
        >
          &times;
        </button>

        <h3 className="text-xl font-semibold mb-4">Leave a Comment</h3>

        <div className="flex items-center mb-3">
          {[1, 2, 3, 4, 5].map((star) => (
            <span
              key={star}
              className={`cursor-pointer text-2xl ${
                rating >= star ? "text-yellow-400" : "text-gray-300"
              }`}
              onClick={() => setRating(star)}
            >
              ★
            </span>
          ))}
        </div>

        <textarea
          rows={4}
          className="w-full p-2 border rounded mb-4 text-sm"
          placeholder="Write your comment here..."
          value={comment}
          onChange={(e) => setComment(e.target.value)}
        />

        <div className="flex justify-end gap-2">
          <button
            onClick={onClose}
            className="px-4 py-2 text-sm text-gray-700 border rounded"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            className="bg-blue-600 text-white px-4 py-2 rounded text-sm"
          >
            Submit
          </button>
        </div>
      </div>
    </div>
  );
};

export default CommentModal;
