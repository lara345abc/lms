import React, { useState } from "react";

const VideoUpload = () => {
  const [videoDuration, setVideoDuration] = useState(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const video = document.createElement("video");
    video.preload = "metadata";

    video.onloadedmetadata = () => {
      window.URL.revokeObjectURL(video.src);
      const durationInSeconds = video.duration;
      setVideoDuration(durationInSeconds.toFixed(2)); // optional: round to 2 decimals
    };

    video.src = URL.createObjectURL(file);
  };

  return (
    <div>
      <input type="file" accept="video/*" onChange={handleFileChange} />
      {videoDuration && <p>Duration: {videoDuration} seconds</p>}
    </div>
  );
};

export default VideoUpload;
