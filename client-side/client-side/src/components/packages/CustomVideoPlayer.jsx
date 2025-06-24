import React, { useEffect, useRef, useState } from 'react';
import ReactPlayer from 'react-player';

const CustomVideoPlayer = ({ url }) => {
  const playerRef = useRef(null);
  const [playing, setPlaying] = useState(true);
  const [playbackRate, setPlaybackRate] = useState(1);

  const handleSeek = (seconds) => {
    const currentTime = playerRef.current.getCurrentTime();
    playerRef.current.seekTo(currentTime + seconds);
  };

  const togglePlay = () => setPlaying(!playing);

  return (
    <div className="space-y-4">
      <div className="aspect-video rounded-xl overflow-hidden">
        <ReactPlayer
          ref={playerRef}
          url={url}
          playing={playing}
          controls={false}
          playbackRate={playbackRate}
          width="100%"
          height="100%"
        />
      </div>

      {/* Custom Controls */}
      <div className="flex items-center justify-center gap-4 flex-wrap">
        <button
          onClick={() => handleSeek(-10)}
          className="bg-gray-200 px-3 py-1 rounded hover:bg-gray-300"
        >
          ⏪ 10s
        </button>

        <button
          onClick={togglePlay}
          className="bg-indigo-600 text-white px-3 py-1 rounded hover:bg-indigo-700"
        >
          {playing ? 'Pause ⏸' : 'Play ▶'}
        </button>

        <button
          onClick={() => handleSeek(10)}
          className="bg-gray-200 px-3 py-1 rounded hover:bg-gray-300"
        >
          ⏩ 10s
        </button>

        <select
          value={playbackRate}
          onChange={(e) => setPlaybackRate(parseFloat(e.target.value))}
          className="bg-white border border-gray-300 rounded px-2 py-1"
        >
          {[0.5, 1, 1.25, 1.5, 2].map((rate) => (
            <option key={rate} value={rate}>
              {rate}x
            </option>
          ))}
        </select>
      </div>
    </div>
  );
};

export default CustomVideoPlayer;
