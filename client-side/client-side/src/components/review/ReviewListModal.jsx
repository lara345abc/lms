import React, { useEffect, useState } from "react";
import toast from "react-hot-toast";
import reviewService from "../../services/reviewService";

const ReviewListModal = ({ isOpen, onClose, targetType, targetId }) => {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isOpen) return;

    const fetchReviews = async () => {
      setLoading(true);
      try {
        const { data } = await reviewService.getReviewsByTarget(targetType, targetId);
        setReviews(data);
      } catch (err) {
        toast.error("Failed to load reviews");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchReviews();
  }, [isOpen, targetType, targetId]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 z-50 flex justify-center items-center">
      <div className="bg-white dark:bg-gray-900 rounded-xl p-6 w-full max-w-xl shadow-lg relative">
        <button
          onClick={onClose}
          className="absolute top-2 right-3 text-gray-600 dark:text-gray-300 text-lg"
        >
          ✖
        </button>

        <h2 className="text-xl font-semibold mb-4">Reviews for {targetType}</h2>

        {loading ? (
          <p className="text-center text-gray-500">Loading reviews...</p>
        ) : reviews.length === 0 ? (
          <p className="text-center text-gray-500">No reviews available.</p>
        ) : (
          <div className="space-y-4 max-h-[60vh] overflow-y-auto">
            {reviews.map((review) => (
              <div
                key={review.id}
                className="border rounded-lg p-3 bg-gray-50 dark:bg-gray-800"
              >
                <div className="flex justify-between items-center mb-1">
                  <span className="font-medium text-indigo-700 dark:text-indigo-300">
                    {review.userName}
                  </span>
                  <span className="text-sm text-yellow-600 dark:text-yellow-400">
                    ⭐ {review.presentationRating}
                  </span>
                </div>
                <p className="text-gray-700 dark:text-gray-300 text-sm">
                  {review.reviewText || "No comment provided"}
                </p>
                <p className="text-xs text-gray-400 mt-1">
                  {new Date(review.createdAt).toLocaleString()}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default ReviewListModal;
