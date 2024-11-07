import React from 'react';

/**A popup banner that is used throughout for success and error messages.
 * ChatGPT was used for the basic structure with bootstrap classes,
 * and we added type and content parameters for customization.*/
function NotificationPopup({type, content, setParentMessage}) {  
  return (
    <>
      <div className={type === "error" ? "alert alert-danger alert-dismissible fade show" : "alert alert-success alert-dismissible fade show"} role="alert">
        {type === "error" ? <strong>Error:</strong> : <strong>Success:</strong>} {content}
        <button
          type="button"
          className="btn-close"
          aria-label="Close"
          onClick={() => setParentMessage({type:"none", content:content})}
        ></button>
      </div>
    </>
  );
}

export default NotificationPopup