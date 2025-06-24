import React, { useState } from "react";
import toast from "react-hot-toast";
import reviewService from "../../services/reviewService";
import { FaStar } from "react-icons/fa";

const ReviewModal = ({ isOpen, onClose, targetType, targetId }) => {
  const [reviewText, setReviewText] = useState("");
  const [presentationRating, setPresentationRating] = useState(0);
  const [hover, setHover] = useState(0);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    try {
      setLoading(true);
      await reviewService.createReview({ targetType, targetId, presentationRating, reviewText });
      toast.success("Review submitted!");
      onClose();
    } catch (error) {
      toast.error("Failed to submit review");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
      <div className="bg-white dark:bg-gray-900 p-6 rounded-xl w-full max-w-md shadow-lg relative">
        <h2 className="text-xl font-semibold mb-4">Submit Review</h2>

        {/* Star Rating */}
        <label className="block mb-2 text-sm font-medium">Presentation Rating</label>
        <div className="flex mb-4">
          {[1, 2, 3, 4, 5].map((star) => (
            <FaStar
              key={star}
              size={24}
              className={`cursor-pointer transition-colors ${
                star <= (hover || presentationRating) ? "text-yellow-400" : "text-gray-300"
              }`}
              onClick={() => setPresentationRating(star)}
              onMouseEnter={() => setHover(star)}
              onMouseLeave={() => setHover(0)}
            />
          ))}
        </div>

        {/* Review Textarea */}
        <label className="block mb-2 text-sm font-medium">Your Review</label>
        <textarea
          value={reviewText}
          onChange={(e) => setReviewText(e.target.value)}
          className="w-full p-2 border rounded-md mb-4 h-24"
        />

        {/* Buttons */}
        <div className="flex justify-end gap-2">
          <button onClick={onClose} className="px-4 py-2 rounded bg-gray-400 text-white">Cancel</button>
          <button
            onClick={handleSubmit}
            disabled={loading || presentationRating === 0}
            className="px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50"
          >
            {loading ? "Submitting..." : "Submit"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ReviewModal;
