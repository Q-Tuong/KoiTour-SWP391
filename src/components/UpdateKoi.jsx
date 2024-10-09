import React, { useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";

function UpdateKoi({ setIsOpen, open, koi }) {
	const [category, setCategory] = useState("");
	const [description, setDescription] = useState("");
	const [color, setColor] = useState("");
	const [weight, setWeight] = useState("");
	const [size, setSize] = useState("");
	const [origin, setOrigin] = useState("");
	const [image, setImage] = useState("");

	const handleClose = () => {
		setIsOpen(false);
	};

	async function update() {}

	return (
		<>
			<Modal size="xl" centered show={open} onHide={handleClose}>
				<Modal.Header closeButton>
					<Modal.Title>Update koi</Modal.Title>
				</Modal.Header>
				<Modal.Body>
					<Form>
						<Form.Group className="mb-3" controlId="formGroupCategory">
							<Form.Label>Category</Form.Label>
							<Form.Control type="text" value={category} placeholder="Category" onChange={(e) => setCategory(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupDesc">
							<Form.Label>Description</Form.Label>
							<Form.Control type="text" value={description} placeholder="Description" onChange={(e) => setDescription(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupColor">
							<Form.Label>Color</Form.Label>
							<Form.Control type="text" value={color} placeholder="Color" onChange={(e) => setColor(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupWeight">
							<Form.Label>Weight</Form.Label>
							<Form.Control type="text" value={weight} placeholder="Weight" onChange={(e) => setWeight(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupSize">
							<Form.Label>Size</Form.Label>
							<Form.Control type="text" value={size} placeholder="Size" onChange={(e) => setSize(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupOrigin">
							<Form.Label>Origin</Form.Label>
							<Form.Control type="text" value={origin} placeholder="Origin" onChange={(e) => setOrigin(e.target.value)} />
						</Form.Group>
						<Form.Group className="mb-3" controlId="formGroupImage">
							<Form.Label>Image</Form.Label>
							<Form.Control type="file" value={image} placeholder="ImageURL" onChange={(e) => setImage(e.target.files[0])} />
						</Form.Group>
					</Form>
				</Modal.Body>
				<Modal.Footer>
					<Button onClick={update}>Update</Button>
					{/* <Button type="reset">Reset</Button> */}
					<Button onClick={handleClose}>Cancel</Button>
				</Modal.Footer>
			</Modal>
		</>
	);
}

export default UpdateKoi;
