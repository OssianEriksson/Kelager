package main.rendering;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import main.Particle;
import main.Utils;
import main.parameters.SimulationOptions;

public class StaticSwingRenderer extends JPanel implements Renderer {

	private static final long serialVersionUID = 1L;

	private final JFrame frame = new JFrame();

	private final Matrix4f viewProjectionMatrix;
	private SimulationOptions opts;
	private Particle[] particles;
	private Vector4f[] projections;
	private double particleSize;
	private final double s;
	private final double V;

	public StaticSwingRenderer(Matrix4f viewProjectionMatrix, double V, double s) {
		this.viewProjectionMatrix = viewProjectionMatrix;
		this.V = V;
		this.s = s;
	}

	@Override
	public void init(Particle[] particles, SimulationOptions opts) {
		this.particles = particles;
		this.opts = opts;
		
		System.setProperty("sun.java2d.opengl", "true");
		
		projections = new Vector4f[particles.length];
		for (int i = 0; i < projections.length; i++) {
			projections[i] = new Vector4f();
		}

		double x = opts.simulationParameters.kernelParticles;
		particleSize = s * Math.pow(3 * V * x / (4 * Math.PI * particles.length), 1.0 / 3.0);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(960, 720);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Kelager - " + particles.length + " particles, dt = " + opts.simulationParameters.dt);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	@Override
	public void render() {
		frame.revalidate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D g = (Graphics2D) graphics;
		Color c = opts.fluidParameters.color;
		float cR = c.getRed() / 255f;
		float cG = c.getGreen() / 255f;
		float cB = c.getBlue() / 255f;
		
		float fov = viewProjectionMatrix.perspectiveFov();
		
		float t = Math.min(getWidth(), getHeight()) / 2;
		double u = t * particleSize / Math.tan(fov / 2);
		
		int renderedParticleCount = 0;
		Vector4f[] renderedParticles = new Vector4f[particles.length];
		
		float farZ = 0;
		float nearZ = Float.MAX_VALUE;
		
		for (int i = 0; i < particles.length; i++) {
			Particle p = particles[i];
			Vector4f proj = projections[i];
			
			viewProjectionMatrix.transform((float) p.r.x, (float) p.r.y, (float) p.r.z, 1, proj);
			proj.set(proj.x / proj.w, proj.y / proj.w, proj.z / proj.w, proj.w);
			
			if (proj.z >= -1 && proj.z <= 1) {
				renderedParticles[renderedParticleCount++] = proj;
				farZ = Math.max(farZ, proj.w);
				nearZ = Math.min(nearZ, proj.w);
			}
		}
		
		Arrays.sort(renderedParticles, 0, renderedParticleCount, (r0, r1) -> {
			return Utils.sign(r1.w - r0.w);
		});

		for (int i = 0; i < renderedParticleCount; i++) {	
			Vector4f proj = renderedParticles[i];
			
			float colorFactor = ((farZ - proj.w) / (farZ - nearZ) + 1) / 2;
			Color color = new Color(cR * colorFactor, cG * colorFactor, cB * colorFactor);
			g.setColor(color);
			
			double size = u / proj.w;
			
			double x = proj.x * t - size / 2 + getWidth() / 2;
			double y = proj.y * t - size / 2 + getHeight() / 2;
	
			g.fillOval((int) Math.round(x), (int) Math.round(y), (int) Math.round(size), (int) Math.round(size));
		}
	}

	@Override
	public void delete() {
		frame.setVisible(false);
	}

}
