import { useState, useEffect } from 'react';

export const useDelay = (delay) => {
  const [timer, setTimer] = useState(null);
  const [open, setOpen] = useState(false);
  const [previewOpen, setPreviewOpen] = useState(false);

  useEffect(() => {
    if (!open) {
      setTimer(
        setTimeout(() => {
          setPreviewOpen(false);
        }, delay)
      );
    }

    if (open) {
      setTimer(null);
      setPreviewOpen(true);
      clearTimeout(timer);
    }
  }, [open]);

  return [previewOpen, setOpen];
};
