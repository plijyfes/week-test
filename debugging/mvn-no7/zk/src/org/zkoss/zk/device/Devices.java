/* Devices.java

	Purpose:
		
	Description:
		
	History:
		Tue May 15 14:09:16     2007, Created by tomyeh

Copyright (C) 2007 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
*/
package org.zkoss.zk.device;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

import org.zkoss.lang.Classes;
import org.zkoss.idom.Element;
import org.zkoss.idom.util.IDOMs;
import org.zkoss.io.Files;

import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.WebApps;

/**
 * A manager of devices ({@link Device}).
 *
 * @author tomyeh
 * @since 2.4.0
 */
public class Devices {
	private Devices() {}

	/** Map(String type, DeviceInfo info or Device device). */
	private static final Map<String, Object> _devmap = new LinkedHashMap<String, Object>(4);
	/** A list of devices registered. It is a duplicated info (of _devmap)
	 * but used to improve the performance.
	 */
	private static Device[] _devs = new Device[0];

	/** Returns the device for the specified desktop type.
	 *
	 * @param deviceType the device type, such as ajax, xml and mil.
	 * @exception DeviceNotFoundException if not found.
	 * @since 3.0.0
	 */
	public static final Device getDevice(String deviceType)
	throws DeviceNotFoundException {
		final Object o; //null, Device or DeviceInfo
		synchronized (_devmap) {
			o = _devmap.get(deviceType);
		}

		if (o instanceof Device)
			return (Device)o;
		if (o == null)
			throw new DeviceNotFoundException(deviceType, MZk.NOT_FOUND, deviceType);

		final Device device = ((DeviceInfo)o).newDevice(deviceType);
		final List<Device> devs = new LinkedList<Device>();
		synchronized (_devmap) {
			final Object old = _devmap.put(deviceType, device);
			if (old != o)
				_devmap.put(deviceType, old); //changed by someone else; so restore

			for (Object d: _devmap.values()) {
				if (d instanceof Device)
					devs.add((Device)d);
			}
		}
		_devs = devs.toArray(new Device[devs.size()]);
		return device;
	}
	/** Returns the device for the specified client.
	 * It invokes {@link Device#isCompatible} to return the correct device.
	 *If all devices returns null (means unknown), one of the devices returning
	 *null is returned. If all devices returns Boolean.FALSE,
	 *DeviceNotFoundException is thrown.
	 *
	 * @param userAgent represents a client.
	 * It is the user-agent header for HTTP-base client.
	 * @see org.zkoss.zk.ui.Execution#getUserAgent
	 * @exception DeviceNotFoundException if not found.
	 * @since 3.0.0
	 */
	public static final Device getDeviceByClient(String userAgent)
	throws DeviceNotFoundException {
		String[] devTypes;
		synchronized (_devmap) {
			Collection<String> c = _devmap.keySet();
			devTypes = c.toArray(new String[c.size()]);
		}

		Device device = null;
		for (int j = 0; j < devTypes.length; ++j) {
			Device dev;
			try {
				dev = getDevice(devTypes[j]);
			} catch (Throwable ex) {
				continue; //skip
			}

			Boolean b;
			try {
				b = dev.isCompatible(userAgent);
			} catch (Throwable ex) { //backward compatible
				b = null;
			}

			if (b != null) {
				if (b.booleanValue())
					return dev;
			} else if (device == null
			|| "ajax".equals(devTypes[j]) //ajax highest priority
			|| "xml".equals(device.getType())) { //xml lowest priority
				device = dev;
			}
		}
		if (device == null)
			throw new DeviceNotFoundException(userAgent, MZk.NOT_FOUND, userAgent);
		return device;
	}

	/** Tests whether the device for the specified type exists.
	 *
	 * @param deviceType the device type, say, ajax.
	 * @since 2.4.0
	 */
	public static final boolean exists(String deviceType) {
		if (deviceType == null) return false;

		final Object o;
		synchronized (_devmap) {
			o = _devmap.get(deviceType);
		}
		return o instanceof Device
			|| (o != null && ((DeviceInfo)o).isValid());
	}

	/** Adds a device type.
	 *
	 * @param deviceType the device type (a.k.a., the device name).
	 * @param clsnm the device class name
	 * @return the previous class of the device with the same type, if any,
	 * or null if no such device.
	 */
	public static final String add(String deviceType, String clsnm) {
		return add0(deviceType, clsnm);
	}
	/** Adds a device type.
	 *
	 * @param deviceType the device type (a.k.a., the device name).
	 * @param cls the device class
	 * @return the previous class of the device with the same type, if any,
	 * or null if no such device.
	 * @since 3.0.0
	 */
	public static final String add(String deviceType, Class cls) {
		return add0(deviceType, cls);
	}
	private static final String add0(String deviceType, Object cls) {
		if (deviceType == null || deviceType.length() == 0
		|| cls == null)
			throw new IllegalArgumentException();

		synchronized (_devmap) {
			final Object o = _devmap.get(deviceType);
			if (o instanceof DeviceInfo) {
				return ((DeviceInfo)o).setDeviceClass(cls);
			} else if (o instanceof Device) {
				final Device device = (Device)o;
				_devmap.put(deviceType,
					new DeviceInfo(cls,
						device.getUnavailableMessage(),
						device.getServerPushClass()));
				return device.getClass().getName();
			} else {
				_devmap.put(deviceType, new DeviceInfo(cls));
				return null;
			}
		}
	}

	/** Returns the unavailable message for the specified device type.
	 *
	 * <p>The result is the same as the invocation of {@link Device#getUnavailableMessage}
	 * against {@link #getDevice}, but this method will not load the device
	 * if it is not loaded yet.
	 *
	 * @see Device
	 * @see Device#getUnavailableMessage
	 */
	public static final String getUnavailableMessage(String deviceType) {
		final Object o;
		synchronized (_devmap) {
			o = _devmap.get(deviceType);
		}
		return o instanceof Device ? ((Device)o).getUnavailableMessage():
			o instanceof DeviceInfo ? ((DeviceInfo)o).getUnavailableMessage(): null;
	}
	/** Sets the unavailable message for the specified device type.
	 *
	 * @return the previous unavailable message if any.
	 * @see Device
	 * @see Device#setUnavailableMessage
	 */
	public static final String setUnavailableMessage(String deviceType, String msg) {
		if (deviceType == null || deviceType.length() == 0)
			throw new IllegalArgumentException();

		if (msg != null && msg.length() == 0)
			msg = null;

		synchronized (_devmap) {
			final Object o = _devmap.get(deviceType);
			if (o instanceof Device) {
				return ((Device)o).setUnavailableMessage(msg);
			} else if (o instanceof DeviceInfo) {
				return ((DeviceInfo)o).setUnavailableMessage(msg);
			} else {
				final DeviceInfo info = new DeviceInfo();
				_devmap.put(deviceType, info);
				info.setUnavailableMessage(msg);
				return null;
			}
		}
	}

	/** Returns the content that shall be embedded to the output being
	 * generated to the client, or null if no embedded content.
	 *
	 * @since 3.0.6
	 */
	public String getEmbedded(String deviceType) {
		final Object o;
		synchronized (_devmap) {
			o = _devmap.get(deviceType);
		}
		return o instanceof Device ? ((Device)o).getEmbedded():
			o instanceof DeviceInfo ? ((DeviceInfo)o).getEmbedded(): null;
	}
	/** Adds the content that shall be added to the output generated and
	 * sent to the client, when rending a desktop.
	 * What content can be embedded depends on the device.
	 * For Ajax devices, it can be anything that can be placed inside
	 * HTML HEAD, such as JavaScript codes.
	 *
	 * <p>As the method name suggests, the embedded contents are accumulated
	 * and all generated to the output.
	 * @since 3.0.6
	 */
	public static void addEmbedded(String deviceType, String content) {
		if (deviceType == null || deviceType.length() == 0)
			throw new IllegalArgumentException();
		if (content == null || content.length() == 0)
			return; //nothing to do

		synchronized (_devmap) {
			final Object o = _devmap.get(deviceType);
			if (o instanceof Device) {
				((Device)o).addEmbedded(content);
			} else if (o instanceof DeviceInfo) {
				((DeviceInfo)o).addEmbedded(content);
			} else {
				final DeviceInfo info = new DeviceInfo();
				_devmap.put(deviceType, info);
				info.addEmbedded(content);
			}
		}
	}

	/** Returns the class name that implements the server push feature.
	 *
	 * <p>Default: null (the server-push feature is not available).
	 *
	 * <p>The result is the same as the invocation of {@link Device#getServerPushClass}
	 * against {@link #getDevice}, but this method will not load the device
	 * if it is not loaded yet.
	 * @since 3.0.0
	 */
	public static final String getServerPushClass(String deviceType) {
		final Object o;
		synchronized (_devmap) {
			o = _devmap.get(deviceType);
		}
		if (o instanceof Device) {
			final Class cls = ((Device)o).getServerPushClass();
			return cls != null ? cls.getName(): null;
		}
		return o instanceof DeviceInfo ? ((DeviceInfo)o).getServerPushClassName(): null;
	}
	/** Sets the name of the class that implements the server-push feature.
	 *
	 * @param clsnm the class name that implements the server push.
	 * If null, it means no server push is available.
	 * @return the previous class name, or null if not available.
	 * @since 3.0.0
	 */
	public static final String setServerPushClass(String deviceType, String clsnm) {
		return setServerPushClass0(deviceType, clsnm);
	}
	/** Sets the class that implements the server-push feature.
	 *
	 * @param cls the class that implements the server push.
	 * If null, it means no server push is available.
	 * @return the previous class name, or null if not available.
	 * @since 3.0.0
	 */
	public static final String setServerPushClass(String deviceType, Class cls) {
		return setServerPushClass0(deviceType, cls);
	}
	private static final String setServerPushClass0(String deviceType, Object cls) {
		if (deviceType == null || deviceType.length() == 0)
			throw new IllegalArgumentException();

		try {
			synchronized (_devmap) {
				final Object o = _devmap.get(deviceType);
				if (o instanceof Device) {
					final Class old = ((Device)o).setServerPushClass(
						cls instanceof Class ? (Class)cls:
						cls != null ? Classes.forNameByThread((String)cls): null);
					return old != null ? old.getName(): null;
				} else if (o instanceof DeviceInfo) {
					return ((DeviceInfo)o).setServerPushClass(cls);
				} else {
					final DeviceInfo info = new DeviceInfo();
					_devmap.put(deviceType, info);
					info.setServerPushClass(cls);
					return null;
				}
			}
		} catch (ClassNotFoundException ex) {
			throw new UiException("Class not found: "+cls);
		}
	}

	/** Returns the name and version of the client if the given user agent
	 * matches any of the devices, or null if not matched or it is a standard
	 * browser request.
	 * <p>It iterates all devices and invokes {@link Device#matches} one-by-one,
	 * then returns the first non-null return, if any.
	 * @param userAgent represents a client (i.e., HTTP's user-agent).
	 * @return a pair of objects or null.
	 * The first element of the pair is the name of the client (String),
	 * the second element is the version (Double, such as 3.5).
	 * @since 6.0.0
	 */
	public static final Object[] matches(String userAgent) {
		for (int j = 0; j < _devs.length; ++j) {
			Object[] inf = _devs[j].matches(userAgent);
			if (inf != null)
				return inf;
		}
		return null;
	}

	/** Adds a device based on the XML declaration.
	 *
	 * <pre><code>
&lt;device-config&gt;
  &lt;device-type&gt;superajax&lt;/device-type&gt;
  &lt;device-class&gt;my.MyDevice&lt;/device-class&gt;
  &lt;unavailable-message&gt;error message&lt;/unavailable-message&gt;
  &lt;server-push-class&gt;my.MyServerPush&lt;/server-push-class&gt;
&lt;/device-config&gt;
	 * </code></pre>
	 *
	 * @param config the XML element called zscript-config
	 */
	public static final void add(Element config) {
		//Spec: it is OK to declare an nonexistent device
		final String deviceType =
			IDOMs.getRequiredElementValue(config, "device-type");

		String s = config.getElementValue("device-class", true);
		if (s != null)
			add(deviceType, s);

		s = config.getElementValue("unavailable-message", true);
		if (s != null)
			setUnavailableMessage(deviceType, s);

		s = config.getElementValue("server-push-class", true);
		if (s != null)
			setServerPushClass(deviceType, s);

		for (Iterator it = config.getElements("embed").iterator();
		it.hasNext();) {
			addEmbedded(deviceType, ((Element)it.next()).getText(true));
		}
	}
	
	/** Loads the content of a javascript file as a String.
	 * @since 5.0.11
	 */
	public static String loadJavaScript(Execution exec, String path)
	throws IOException {
		path = exec.locate(path);
		//ZK-1345: use WebApps.getCurrent() instead of exec.getDesktop().getWebApp()
		InputStream is = WebApps.getCurrent().getResourceAsStream(path);
		if (is == null)
			throw new UiException("Unable to load "+path);
		final byte[] bs = Files.readAll(is);
		Files.close(is);
		return new String(bs, "UTF-8"); //UTF-8 is assumed
	}
	
	/** Device info.
	 */
	private static class DeviceInfo implements DeviceConfig {
		/** The class or class name of {@link Device}
		 * of the device's implementation.
		 */
		private Object _dvcls;
		private String _uamsg;
		/** The class name or class of {@link ServerPush}.
		 */
		private Object _spushcls;
		private String _embed;

		private DeviceInfo() {
		}
		private DeviceInfo(Object deviceClass) {
			_dvcls = deviceClass;
		}
		private DeviceInfo(Object deviceClass, String unavailable,
		Class spushcls) {
			_dvcls = deviceClass;
			_uamsg = unavailable;
			_spushcls = spushcls;
		}
		/** Returns whether this device is valid, i.e., defined with a device class.
		 */
		private boolean isValid() {
			return _dvcls != null;
		}
		/** Sets the device class.
		 */
		private String setDeviceClass(Object cls) {
			final Object old = _dvcls;
			_dvcls = cls;
			return old instanceof Class ? ((Class)old).getName(): (String)old;
		}
		public String getUnavailableMessage() {
			return _uamsg;
		}
		public String setUnavailableMessage(String msg) {
			final String old = _uamsg;
			_uamsg = msg != null && msg.length() > 0 ? msg: null;
			return old;
		}
		/**
		 * @param cls the class name or class of the server push.
		 */
		public String setServerPushClass(Object cls) {
			final Object old = _spushcls;
			_spushcls = cls;
			return old instanceof Class ? ((Class)old).getName(): (String)old;
		}
		public String getServerPushClassName() {
			return _spushcls instanceof Class ? ((Class)_spushcls).getName(): (String)_spushcls;
		}
		public Class getServerPushClass() {
			try {
				return _spushcls instanceof Class ? (Class)_spushcls:
					_spushcls != null ?
						Classes.forNameByThread((String)_spushcls): null;
			} catch (ClassNotFoundException ex) {
				throw new UiException("Class not found: "+_spushcls);
			}
		}
		public void addEmbedded(String content) {
			if (content != null && content.length() > 0)
				_embed = _embed != null ? _embed + '\n' + content: content;
		}
		public String getEmbedded() {
			return _embed;
		}

		/** Creates a device based on this device info.
		 */
		private Device newDevice(String deviceType) {
			if (_dvcls == null) //possible
				throw new DeviceNotFoundException(deviceType, MZk.NOT_FOUND, deviceType);

			try {
				final Class cls;
				if (_dvcls instanceof Class) {
					cls = (Class)_dvcls;
				} else {
					cls = Classes.forNameByThread((String)_dvcls);
					if (!Device.class.isAssignableFrom(cls))
						throw new IllegalArgumentException(cls+" must implements "+Device.class);
				}

				final Device device = (Device)cls.newInstance();
				device.init(deviceType, this);
				return device;
			} catch (Exception ex) {
				throw UiException.Aide.wrap(ex, "Unable to create "+_dvcls);
			}
		}
	}
}
