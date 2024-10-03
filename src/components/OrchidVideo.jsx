import React from "react";
import { Button, Modal } from "react-bootstrap";

function OrchidVideo({ setIsOpen, open, orchid }) {
	const handleClose = () => setIsOpen(false);

	return (
		<>
			<Modal centered show={open} onHide={handleClose}>
				<Modal.Header closeButton>
					<Modal.Title>{orchid.name}</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<iframe
						width="466"
						height="256"
						src={orchid.video}
						title={orchid.name}
						frameborder="0"
						allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
						referrerpolicy="strict-origin-when-cross-origin"
						allowfullscreen></iframe>
				</Modal.Body>
				<Modal.Footer>
					<Button variant="secondary" onClick={handleClose}>
						Close
					</Button>
				</Modal.Footer>
			</Modal>
		</>
	);
}

export default OrchidVideo;
