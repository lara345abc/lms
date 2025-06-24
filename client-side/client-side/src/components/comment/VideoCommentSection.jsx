import React, { useEffect, useState, useCallback } from "react";
import { FaStar } from "react-icons/fa";
import commentService from "../../services/commentService";

const VideoCommentsSection = ({ videoId }) => {
  const [summary, setSummary] = useState(null);
  const [distribution, setDistribution] = useState({});
  const [comments, setComments] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const [filterRating, setFilterRating] = useState(0);

  const fetchInitialData = async () => {
    try {
      setLoading(true);
      const [summaryRes, distRes] = await Promise.all([
        commentService.getCommentSummary(videoId),
        commentService.getRatingDistribution(videoId),
      ]);
      setSummary(summaryRes.data);
      setDistribution(distRes.data.ratingCounts);
    } catch (err) {
      console.error("Error loading summary/distribution", err);
    } finally {
      setLoading(false);
    }
  };

  const fetchComments = useCallback(
    async (currentPage) => {
      if (!hasMore || loading) return;
      try {
        setLoading(true);
        const res = await commentService.getPagedCommentsByVideo(videoId, currentPage, 5);
        const newComments = res.data.content;
        setComments((prev) => [...prev, ...newComments]);
        setHasMore(!res.data.last);
        setPage(currentPage + 1);
      } catch (err) {
        console.error("Error loading paged comments", err);
      } finally {
        setLoading(false);
      }
    },
    [videoId, hasMore, loading]
  );

  useEffect(() => {
    if (videoId && !isNaN(Number(videoId))) {
      setComments([]);
      setPage(0);
      setHasMore(true);
      fetchInitialData();
      fetchComments(0); // load first page
    }
  }, [videoId]);

  const filtered = filterRating
    ? comments.filter((c) => c.rating === filterRating)
    : comments;

  const getPercent = (count) =>
    !summary?.totalComments || !count
      ? 0
      : ((count / summary.totalComments) * 100).toFixed(1);

  return (
    <div className="mt-6 bg-white p-4 rounded shadow-md">
      <h2 className="text-lg font-semibold mb-2">Video Comments</h2>

      {summary && (
        <div className="mb-3">
          <div className="flex items-center gap-2 text-yellow-500">
            <span className="text-sm font-medium text-gray-800">
              Average Rating:
            </span>
            {[...Array(5)].map((_, i) => (
              <FaStar
                key={i}
                className={
                  i < Math.round(summary.averageRating)
                    ? "text-yellow-500"
                    : "text-gray-300"
                }
              />
            ))}
            <span className="ml-2 text-sm text-gray-600">
              ({summary.totalComments} reviews)
            </span>
          </div>

          <div className="mt-2 space-y-1">
            {[5, 4, 3, 2, 1].map((r) => (
              <div key={r} className="flex items-center gap-2">
                <button
                  onClick={() =>
                    setFilterRating(filterRating === r ? 0 : r)
                  }
                  className={`text-sm font-medium w-12 text-left ${
                    filterRating === r
                      ? "text-blue-600 underline"
                      : "text-gray-700"
                  }`}
                >
                  {r} ★
                </button>
                <div className="w-full h-3 bg-gray-200 rounded">
                  <div
                    className="h-3 bg-yellow-400 rounded transition-all duration-300"
                    style={{ width: `${getPercent(distribution[r])}%` }}
                  ></div>
                </div>
                <span className="text-sm text-gray-500">
                  ({distribution[r] || 0})
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {filtered.length === 0 ? (
        <p className="text-gray-500 mt-2">No comments to display.</p>
      ) : (
        <ul className="space-y-3 mt-4">
          {filtered.map((c) => (
            <li key={c.id} className="border-b pb-2 animate-fadeIn">
              <div className="flex items-center gap-2 text-sm text-gray-800 font-semibold">
                {c.userName}
                <span className="flex items-center gap-1 text-yellow-500 ml-2">
                  {[...Array(5)].map((_, i) => (
                    <FaStar
                      key={i}
                      className={
                        i < c.rating ? "text-yellow-500" : "text-gray-300"
                      }
                    />
                  ))}
                </span>
              </div>
              <p className="text-sm text-gray-700">{c.comment}</p>
            </li>
          ))}
        </ul>
      )}

      {!filterRating && hasMore && (
        <div className="text-center mt-4">
          <button
            onClick={() => fetchComments(page)}
            className="px-4 py-2 bg-blue-500 text-white text-sm rounded hover:bg-blue-600 disabled:opacity-50"
            disabled={loading}
          >
            {loading ? "Loading..." : "Load More Comments"}
          </button>
        </div>
      )}
    </div>
  );
};

export default VideoCommentsSection;
