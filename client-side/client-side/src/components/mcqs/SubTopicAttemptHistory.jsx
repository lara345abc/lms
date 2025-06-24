import React, { useEffect, useState } from "react";
import mcqAttemptService from "../../services/mcqAttemptService";
import { format } from "date-fns";
import toast from "react-hot-toast";
import { useParams } from "react-router-dom";
import SubTopicDetailsViewer from "../topic/SubTopicDetailsViewer";

const SubTopicAttemptHistory = () => {
    const { subTopicId } = useParams();
    const [attempts, setAttempts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchAttempts = async () => {
            try {
                const response = await mcqAttemptService.getAttemptsBySubTopicId(subTopicId);
                setAttempts(response.data);
            } catch (err) {
                toast.error("Failed to fetch attempt history");
            } finally {
                setLoading(false);
            }
        };

        if (subTopicId) fetchAttempts();
    }, [subTopicId]);

    if (loading) return <p className="p-4">Loading attempt history...</p>;

    if (attempts.length === 0)
        return <p className="p-4 text-gray-600">No attempts found for this SubTopic.</p>;

    return (
        <div className="mt-6">
            <h3 className="text-lg font-semibold mb-2">Attempt History</h3>
            <SubTopicDetailsViewer subTopicIds={subTopicId.split(",").map(Number)} />
            <table className="w-full border text-sm">
                <thead className="bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-100">
                    <tr>
                        <th className="p-2 border">#</th>
                        <th className="p-2 border">Attempt Number</th>
                        <th className="p-2 border">Score</th>
                        <th className="p-2 border">Total Marks</th>
                        <th className="p-2 border">Attempted At</th>
                    </tr>
                </thead>
                <tbody>
                    {attempts.map((attempt, index) => (
                        <tr key={attempt.id} className="text-center">
                            <td className="p-2 border">{index + 1}</td>
                            <td className="p-2 border">{attempt.attemptNumber}</td>
                            <td className="p-2 border">{attempt.score}</td>
                            <td className="p-2 border">{attempt.totalMarks}</td>
                            <td className="p-2 border">
                                {format(new Date(attempt.attemptedAt), "dd MMM yyyy, hh:mm a")}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default SubTopicAttemptHistory;
