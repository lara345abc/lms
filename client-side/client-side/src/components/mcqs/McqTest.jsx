import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import mcqService from "../../services/mcqService";
import toast from "react-hot-toast";
import SubTopicDetailsViewer from "../topic/SubTopicDetailsViewer";
import mcqAttemptService from "../../services/mcqAttemptService";

const McqTest = () => {
    const { subTopicIds } = useParams();
    const [mcqs, setMcqs] = useState([]);
    const [answers, setAnswers] = useState({});
    const [showResult, setShowResult] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchMcqs = async () => {
            setLoading(true)
            try {
                const ids = subTopicIds.split(",").map(Number);
                const response = await mcqService.getBySubTopic(ids);
                setMcqs(response.data);
                setLoading(false);
            } catch (err) {
                toast.error("Failed to load MCQs");
            }
        };
        fetchMcqs();
    }, [subTopicIds]);

    const handleOptionSelect = (questionId, option) => {
        setAnswers((prev) => {
            const current = prev[questionId] || [];
            const updated = current.includes(option)
                ? current.filter((opt) => opt !== option)
                : [...current, option];
            return { ...prev, [questionId]: updated };
        });
    };

    const calculateScore = () => {
        let correct = 0;

        mcqs.forEach((mcq) => {
            const correctOptions = mcq.correctOption.split(",").map((o) => o.trim()).sort();
            const selectedOptions = (answers[mcq.id] || []).sort();

            if (
                correctOptions.length === selectedOptions.length &&
                correctOptions.every((opt, i) => opt === selectedOptions[i])
            ) {
                correct++;
            }
        });

        return { correct, total: mcqs.length };
    };


    if (mcqs.length === 0) return <p className="p-4">Questions will be added</p>;



    return (
        <div className="p-6 max-w-4xl mx-auto">
            <h2 className="text-2xl font-bold mb-4">MCQ Test</h2>
            <SubTopicDetailsViewer subTopicIds={subTopicIds.split(",").map(Number)} />
            {!showResult ? (
                <form
                    className="space-y-6"
                    onSubmit={(e) => {
                        e.preventDefault();
                        const { correct, total } = calculateScore();

                        const subTopicIdList = subTopicIds.split(",").map(Number);
                        const saveAllAttempts = async () => {
                            try {
                                for (const subTopicId of subTopicIdList) {
                                    await mcqAttemptService.createAttempt({
                                        subTopicId,
                                        score: correct,
                                        totalMarks: total
                                    });
                                }
                                toast.success("Test submitted and result saved!");
                                setShowResult(true);
                            } catch (err) {
                                console.error(err)
                                toast.error("Failed to save test attempt");
                            }
                        };
                        saveAllAttempts();
                    }}
                >
                    {mcqs.map((mcq, index) => {
                        const options = JSON.parse(mcq.options);
                        const selected = answers[mcq.id];

                        return (
                            <div key={mcq.id} className="p-4 border rounded bg-white dark:bg-gray-800 shadow">
                                <h3 className="font-semibold mb-2">
                                    Q{index + 1}. {mcq.question}
                                </h3>
                                <div className="space-y-2">
                                    {Object.entries(options)
                                        .filter(([_, value]) => value.trim().toLowerCase() !== "na")
                                        .map(([key, value]) => {
                                            const isChecked = (answers[mcq.id] || []).includes(key);

                                            return (
                                                <label
                                                    key={key}
                                                    className={`block p-2 border rounded cursor-pointer transition ${isChecked ? "bg-blue-100 border-blue-400" : "hover:bg-gray-100"
                                                        }`}
                                                >
                                                    <input
                                                        type="checkbox"
                                                        value={key}
                                                        checked={isChecked}
                                                        onChange={() => handleOptionSelect(mcq.id, key)}
                                                        className="mr-2"
                                                    />
                                                    <strong>{key}.</strong> {value}
                                                </label>
                                            );
                                        })}
                                </div>
                            </div>
                        );
                    })}

                    <div className="text-right">
                        <button
                            type="submit"
                            className="mt-4 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                        >
                            Submit Test
                        </button>
                    </div>
                </form>
            ) : (
                <div className="p-4 border rounded bg-green-50 text-green-800">
                    <h3 className="text-lg font-semibold">Test Completed</h3>
                    <p className="mt-2">
                        You scored {calculateScore().correct} out of {calculateScore().total}
                    </p>
                    <button
                        onClick={() => {
                            setShowResult(false);
                            setAnswers({});
                        }}
                        className="mt-4 bg-indigo-600 text-white px-4 py-2 rounded hover:bg-indigo-700"
                    >
                        Retake Test
                    </button>
                </div>
            )}
        </div>
    );
};

export default McqTest;
