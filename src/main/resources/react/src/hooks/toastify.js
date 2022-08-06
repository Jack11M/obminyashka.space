import { toast } from 'react-toastify';

export const showMessage = (message) => {
  toast.dismiss();
  toast.clearWaitingQueue();
  toast(message);
};
