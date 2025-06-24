import React from 'react';

const VideoPlayerModal = ({ isOpen, videoUrl, onClose }) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-4 max-w-2xl w-full relative">
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 text-gray-600 hover:text-black"
                >
                    ✕
                </button>
                <video src={videoUrl} controls autoPlay className="w-full h-96 rounded" />
            </div>
        </div>
    );
};

export default VideoPlayerModal;
